package com.proyectofinal.proyectofinal.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.data.embedding.Embedding;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpenAiService {

    private static final String META_FILENAME_KEY = "filename";

    private final ChatLanguageModel chatModel; // Modelo de chat (GPT-4o-mini)
    private final EmbeddingModel embeddingModel; // text-embedding-3-small
    private final EmbeddingStore<TextSegment> store; // Almacén en memoria

    public OpenAiService(ChatLanguageModel chatModel,
            EmbeddingModel embeddingModel,
            EmbeddingStore<TextSegment> store) {
        this.chatModel = chatModel;
        this.embeddingModel = embeddingModel;
        this.store = store;
    }

    // Indexa el texto, genera chunks, obtiene embeddings y los guarda
    public void indexFile(String text, String filename) {
        DocumentSplitter splitter = DocumentSplitters.recursive(800, 200);
        Metadata meta = Metadata.from(META_FILENAME_KEY, filename);
        Document doc = Document.from(text, meta);

        List<TextSegment> segments = splitter.split(doc);
        if (!segments.isEmpty()) {
            List<Embedding> embeddings = embeddingModel.embedAll(segments).content();
            for (int i = 0; i < segments.size(); i++) {
                store.add(embeddings.get(i), segments.get(i));
            }
        }
    }

    // Genera las respuestas usando el contexto más relevante
    public String answerFromContext(String question, int maxResults, String systemPrompt) {
        String context = buildContext(question, maxResults);
        return generateAnswer(question, context, systemPrompt);
    }

    // Construye el contexto más relevante a partir de los embeddings almacenados
    private String buildContext(String question, int maxResults) {
        Embedding queryEmbedding = embeddingModel.embed(question).content();
        EmbeddingSearchResult<TextSegment> result = store.search(
                EmbeddingSearchRequest.builder()
                        .queryEmbedding(queryEmbedding)
                        .maxResults(maxResults)
                        .build());

        return result.matches().stream()
                .map(m -> m.embedded().text())
                .collect(Collectors.joining("\n---\n"));
    }

    // Devuelve las fuentes de donde se obtuvo la información
    public List<String> topSources(String question, int maxResults) {
        Embedding queryEmbedding = embeddingModel.embed(question).content();
        EmbeddingSearchResult<TextSegment> result = store.search(
                EmbeddingSearchRequest.builder()
                        .queryEmbedding(queryEmbedding)
                        .maxResults(maxResults)
                        .build());

        return result.matches().stream()
                .map(m -> {
                    Metadata meta = m.embedded().metadata();
                    String name = meta != null ? meta.getString(META_FILENAME_KEY) : null;
                    return name != null ? name : "desconocido";
                })
                .distinct()
                .toList();
    }

    // Prompt editable para controlar las respuestas
    private String generateAnswer(String question, String context, String systemPrompt) {
        String userPrompt = "Contexto:\n" + context + "\n\nPregunta: " + question;

        AiMessage aiMessage = chatModel.generate(List.of(
                SystemMessage.from(systemPrompt),
                UserMessage.from(userPrompt)
        )).content();

        return aiMessage.text();
    }
}
