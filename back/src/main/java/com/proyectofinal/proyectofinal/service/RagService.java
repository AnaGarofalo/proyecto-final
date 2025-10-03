package com.proyectofinal.proyectofinal.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
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

    private final FileTextExtractor fileTextExtractor; // Extrae el texto de los archivos
    private final EmbeddingStore<TextSegment> store; // Almacén en memoria de embeddings
    private final EmbeddingModel embeddingModel; // text-embedding-3-small
    private final OpenAiService openAiService; // Servicio para interactuar con OpenAI

    public RagService(FileTextExtractor fileTextExtractor,
            EmbeddingModel embeddingModel,
            EmbeddingStore<TextSegment> store,
            OpenAiService openAiService) {
        this.fileTextExtractor = fileTextExtractor;
        this.store = store;
        this.embeddingModel = embeddingModel;
        this.openAiService = openAiService;
    }

    // Recorre los archivos, extrae texto, genera embeddings y los guarda
    public int ingestFiles(MultipartFile[] files) throws Exception {
        var splitter = DocumentSplitters.recursive(800, 200); // Divide en fragmentos y solapa
        List<TextSegment> segments = new ArrayList<>();

        for (MultipartFile f : files) {
            if (f.isEmpty())
                continue;

            String text = fileTextExtractor.extract(f.getBytes(), f.getOriginalFilename());
            if (text == null)
                text = "";
            text = text.strip();
            if (text.isBlank())
                continue;

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
        return segments.isEmpty() ? 0 : 1;
    }

    // Responde a la pregunta usando los embeddings + OpenAiService
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

        return openAiService.generateAnswer(question, context);
    }

    // Retorna los nombres de los archivos fuente de las respuestas
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