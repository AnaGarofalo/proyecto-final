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

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OpenAiService {

    private static final String DOCUMENT_ID = "document_id";

    private final ChatLanguageModel chatModel; // Modelo de chat (GPT-4o-mini)
    private final EmbeddingModel embeddingModel; // text-embedding-3-small
    private final EmbeddingStore<TextSegment> store; // Embedding store en la base de datos de rag
    private final JdbcTemplate embeddingJdbcTemplate; // Conexión con la base de datos de rag


    public OpenAiService(ChatLanguageModel chatModel,
                         EmbeddingModel embeddingModel,
                         EmbeddingStore<TextSegment> store, JdbcTemplate embeddingJdbcTemplate) {
        this.chatModel = chatModel;
        this.embeddingModel = embeddingModel;
        this.store = store;
        this.embeddingJdbcTemplate = embeddingJdbcTemplate;
    }

    // Indexa el texto, genera chunks, obtiene embeddings y los guarda
    public void indexFile(String text, String documentExternalId) {
        log.info("Indexing document with externalId {}", documentExternalId);
        DocumentSplitter splitter = DocumentSplitters.recursive(800, 200);
        Metadata meta = Metadata.from(DOCUMENT_ID, documentExternalId);
        Document doc = Document.from(text, meta);

        log.info("Splitting document with externalId {}", documentExternalId);
        List<TextSegment> segments = splitter.split(doc);
        if (!segments.isEmpty()) {
            log.info("Creating embeddings for document with externalId {}", documentExternalId);
            List<Embedding> embeddings = embeddingModel.embedAll(segments).content();

            log.info("Saving embeddings for document with externalId {}", documentExternalId);
            for (int i = 0; i < segments.size(); i++) {
                store.add(embeddings.get(i), segments.get(i));
            }
        }
    }

    // Remueve los embeddings nacidos de un documento en particular
    public void removeFile(String documentExternalId) {
        log.info("Removing embeddings for document with external id {}", documentExternalId);

        String sql = "DELETE FROM embeddings WHERE metadata ->> 'document_id' = ?";
        embeddingJdbcTemplate.update(sql, documentExternalId);
    }

    // Genera las respuestas usando el contexto más relevante
    public String answerFromContext(String question, int maxResults, String systemPrompt) {

        String context = buildContext(question, maxResults);
        log.info("Requesting answer from OpenAi: question {}, context {}, systemPrompt {}", question, context, systemPrompt);
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
                    String name = meta != null ? meta.getString(DOCUMENT_ID) : null;
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
