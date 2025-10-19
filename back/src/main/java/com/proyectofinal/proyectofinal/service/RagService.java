package com.proyectofinal.proyectofinal.service;

import com.proyectofinal.proyectofinal.dto.IAResponseDTO;
import com.proyectofinal.proyectofinal.dto.app_user.IngestResponseDTO;
import com.proyectofinal.proyectofinal.utils.IAResponseParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class RagService {

    private final FileTextExtractor fileTextExtractor; // Extrae texto de archivos
    private final OpenAiService openAiService;
    private final SystemPromptService systemPromptService;

    public RagService(FileTextExtractor fileTextExtractor,
                      OpenAiService openAiService,
                      SystemPromptService systemPromptService) {
        this.fileTextExtractor = fileTextExtractor;
        this.openAiService = openAiService;
        this.systemPromptService = systemPromptService;
    }

    // Aca se procesan los archivos subidos
    public IngestResponseDTO ingestFiles(MultipartFile[] files) throws Exception {
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

        return new IngestResponseDTO(processed);
    }

    // Delega la pregunta al servicio de OpenAI
    public IAResponseDTO ask(String question) {
        String systemPrompt = systemPromptService.getLatest().getPrompt();
        String response = openAiService.answerFromContext(question, 5, systemPrompt);
        return IAResponseParser.parse(response);
    }

    // Indica las fuentes más relevantes para la pregunta al servicio de OpenAI
    public List<String> topSources(String question) {
        return openAiService.topSources(question, 5);
    }
}