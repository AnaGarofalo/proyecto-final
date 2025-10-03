package com.proyectofinal.proyectofinal.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpenAiService {

    private final ChatLanguageModel chatModel; // Modelo de chat (GPT-4o-mini)
    private final EmbeddingModel embeddingModel; // text-embedding-3-small
    private final EmbeddingStore<TextSegment> store; // Ahora guarda en memoria

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
        Metadata meta = Metadata.from("filename", filename);
        Document doc = Document.from(text, meta);

        var segments = splitter.split(doc);
        if (!segments.isEmpty()) {
            var embeddings = embeddingModel.embedAll(segments).content();
            for (int i = 0; i < segments.size(); i++) {
                store.add(embeddings.get(i), segments.get(i));
            }
        }
    }

    // Genera las respuestas usando el contexto más relevante
    public String answerFromContext(String question, int maxResults) {
        String context = buildContext(question, maxResults);
        return generateAnswer(question, context);
    }

    // Aca se busca el contexto más relevante
    private String buildContext(String question, int maxResults) {
        var queryEmbedding = embeddingModel.embed(question).content();
        var result = store.search(EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(maxResults)
                .build());

        return result.matches().stream()
                .map(m -> m.embedded().text())
                .collect(Collectors.joining("\n---\n"));
    }

    // Devuelve las fuentes de donde se obtuvo la información
    public List<String> topSources(String question, int maxResults) {
        var queryEmbedding = embeddingModel.embed(question).content();
        var result = store.search(EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(maxResults)
                .build());

        return result.matches().stream()
                .map(m -> {
                    var meta = m.embedded().metadata();
                    var name = meta != null ? meta.getString("filename") : null;
                    return name != null ? name : "desconocido";
                })
                .distinct()
                .toList();
    }

    // Prompt editable para controlar las respuestas
    private String generateAnswer(String question, String context) {
        String prompt = "Responde EXCLUSIVAMENTE usando el siguiente contexto. " +
                "Ignora cualquier conocimiento previo y no corrijas lo que está en el documento. " +
                "Si no está en el contexto, responde 'No lo sé'.\n\n" +
                "Contexto:\n" + context + "\n\n" +
                "Pregunta: " + question + "\nRespuesta:";

        return chatModel.generate(prompt);
    }
}