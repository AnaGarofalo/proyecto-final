package com.proyectofinal.proyectofinal.controller;

import com.proyectofinal.proyectofinal.dto.DocumentDTO;
import com.proyectofinal.proyectofinal.model.Document;
import com.proyectofinal.proyectofinal.service.DocumentService;
import com.proyectofinal.proyectofinal.service.RagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("documents")
public class DocumentController {

    private final DocumentService service;
    private final RagService ragService;

    public DocumentController(DocumentService service, RagService ragService) {
        this.service = service;
        this.ragService = ragService;
    }

    // Subida de un archivos a la base de datos
    @PostMapping("/upload")
    public ResponseEntity<List<DocumentDTO>> upload(@RequestParam("files") MultipartFile[] files) throws Exception {
        List<Document> documents = ragService.ingestFiles(files);
        List<DocumentDTO> dtos = documents.stream()
                .map(DocumentDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping
    public ResponseEntity<List<DocumentDTO>> getAllDocuments() {
        List<DocumentDTO> documentDTOs = service.getAllDocuments()
                .stream()
                .map(DocumentDTO::new)
                .toList();

        return ResponseEntity.ok(documentDTOs);
    }

    @DeleteMapping("/{externalId}")
    public ResponseEntity<DocumentDTO> delete(@PathVariable("externalId") String externalId) {
        Document document = ragService.removeFile(externalId);

        return ResponseEntity.ok(new DocumentDTO(document));
    }

}