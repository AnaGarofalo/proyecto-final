package com.proyectofinal.proyectofinal.service;

import com.proyectofinal.proyectofinal.dto.IAResponseDTO;
import com.proyectofinal.proyectofinal.model.AppUser;
import com.proyectofinal.proyectofinal.model.Document;
import com.proyectofinal.proyectofinal.utils.IAResponseParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RagService {

    private final FileTextExtractor fileTextExtractor; // Extrae texto de archivos
    private final OpenAiService openAiService;
    private final SystemPromptService systemPromptService;
    private final DocumentService documentService;
    private final AppUserService appUserService;

    public RagService(FileTextExtractor fileTextExtractor,
                      OpenAiService openAiService,
                      SystemPromptService systemPromptService, DocumentService documentService, AppUserService appUserService) {
        this.fileTextExtractor = fileTextExtractor;
        this.openAiService = openAiService;
        this.systemPromptService = systemPromptService;
        this.documentService = documentService;
        this.appUserService = appUserService;
    }

    // Aca se procesan los archivos subidos
    public List<Document> ingestFiles(MultipartFile[] files) throws Exception {
        log.info("Ingesting {} files", files.length);
        List<Document> documents = new ArrayList<>();
        AppUser appUser = appUserService.getFromToken();
        for (MultipartFile f : files) {

            if (f.isEmpty()){
                log.warn("File empty");
                continue;
            }

            String text = fileTextExtractor.extract(f.getBytes(), f.getOriginalFilename());
            if (text == null || text.isBlank()){
                log.warn("File empty: {}", f.getOriginalFilename());
                continue;
            }

            Document created = documentService.saveFile(f, appUser);

            try {
                openAiService.indexFile(text, created.getExternalId());
                documents.add(created);
            } catch (Exception e) {
                // If the file embeddings weren't saved, we don't want the document on our database
                log.error("Failed to index {}", f.getOriginalFilename());
                documentService.markAsDeleted(created.getExternalId());

                throw new RuntimeException(e);
            }
        }
        return documents;
    }

    @Transactional
    public Document removeFile(String externalId) {
        log.info("Removing document with external id {}", externalId);
        Document document = documentService.markAsDeleted(externalId);

        openAiService.removeFile(externalId);
        return document;
    }

    // Delega la pregunta al servicio de OpenAI
    public IAResponseDTO ask(String question) {
        String systemPrompt = systemPromptService.getBasePrompt();
        String response = openAiService.answerFromContext(question, 5, systemPrompt);
        return IAResponseParser.parse(response);
    }

    // Indica las fuentes más relevantes para la pregunta al servicio de OpenAI
    public List<String> topSources(String question) {
        return openAiService.topSources(question, 5);
    }
}