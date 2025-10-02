package com.proyectofinal.proyectofinal.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagService {

    private final FileTextExtractor textExtractor;
    private final EmbeddingStore<TextSegment> store;
    private final EmbeddingModel embeddingModel;
    private final ChatLanguageModel chatModel;

    public RagService(FileTextExtractor textExtractor,
                      EmbeddingModel embeddingModel,
                      EmbeddingStore<TextSegment> store,
                      ChatLanguageModel chatModel) {
        this.textExtractor = textExtractor;
        this.store = store;
        this.embeddingModel = embeddingModel;
        this.chatModel = chatModel;
    }

    public int ingestFiles(MultipartFile[] files) throws Exception {
        var splitter = DocumentSplitters.recursive(800, 200);
        List<TextSegment> segments = new ArrayList<>();

        for (MultipartFile f : files) {
            if (f.isEmpty()) continue;
            String text = textExtractor.extract(f.getBytes(), f.getOriginalFilename());
            if (text == null) text = "";
            text = text.strip();
            if (text.isBlank()) continue;

            Metadata meta = Metadata.from("filename", f.getOriginalFilename());
            Document doc = Document.from(text, meta);
            splitter.split(doc).forEach(segments::add);
        }

        if (!segments.isEmpty()) {
            var embeddings = embeddingModel.embedAll(segments).content();
            for (int i = 0; i < segments.size(); i++) {
                store.add(embeddings.get(i), segments.get(i));
            }
        }
        return segments.isEmpty() ? 0 : 1; // número de archivos indexados aproximado
    }

    public String ask(String question) {
    var queryEmbedding = embeddingModel.embed(question).content();
    var result = store.search(EmbeddingSearchRequest.builder()
        .queryEmbedding(queryEmbedding)
        .maxResults(5)
        .build());
    var matches = result.matches();

        var context = matches.stream()
                .map(m -> m.embedded().text())
                .collect(Collectors.joining("\n---\n"));

        String prompt = "Usa el siguiente contexto para responder con precisión. " +
                "Si no está en el contexto, di que no lo sabes.\n\n" +
                "Contexto:\n" + context + "\n\n" +
                "Pregunta: " + question + "\nRespuesta:";

        return chatModel.generate(prompt);
    }

    public List<String> topSources(String question) {
    var queryEmbedding = embeddingModel.embed(question).content();
    var result = store.search(EmbeddingSearchRequest.builder()
        .queryEmbedding(queryEmbedding)
        .maxResults(5)
        .build());
    var matches = result.matches();
        return matches.stream()
                .map(m -> {
                    var meta = m.embedded().metadata();
                    var name = meta != null ? meta.getString("filename") : null;
                    return name != null ? name : "desconocido";
                })
                .distinct()
                .toList();
    }
}