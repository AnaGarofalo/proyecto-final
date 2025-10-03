package com.proyectofinal.proyectofinal.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class RagService {

    private final FileTextExtractor fileTextExtractor; // Extrae texto de archivos
    private final OpenAiService openAiService;

    public RagService(FileTextExtractor fileTextExtractor,
            OpenAiService openAiService) {
        this.fileTextExtractor = fileTextExtractor;
        this.openAiService = openAiService;
    }

    // Aca se procesan los archivos subidos
    public int ingestFiles(MultipartFile[] files) throws Exception {
        int processed = 0;

        for (MultipartFile f : files) {
            if (f.isEmpty())
                continue;

            String text = fileTextExtractor.extract(f.getBytes(), f.getOriginalFilename());
            if (text == null || text.isBlank())
                continue; 

            openAiService.indexFile(text, f.getOriginalFilename());
            processed++;
        }

        return processed;
    }

    // Delega la pregunta al servicio de OpenAI
    public String ask(String question) {
        return openAiService.answerFromContext(question, 5);
    }

    // Indica las fuentes más relevantes para la pregunta al servicio de OpenAI
    public List<String> topSources(String question) {
        return openAiService.topSources(question, 5);
    }
}