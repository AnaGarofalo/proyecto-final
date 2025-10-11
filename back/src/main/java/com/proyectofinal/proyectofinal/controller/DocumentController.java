package com.proyectofinal.proyectofinal.controller;

import com.proyectofinal.proyectofinal.dto.DocumentDTO;
import com.proyectofinal.proyectofinal.model.Document;
import com.proyectofinal.proyectofinal.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("documents")
public class DocumentController {

    private final DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    public ResponseEntity<DocumentDTO> upload(@RequestParam("file") MultipartFile file) throws IOException {
        Document document = service.saveFile(file);

        DocumentDTO dto = DocumentDTO.builder()
                .externalId(document.getExternalId())
                .fileName(document.getFileName())
                .createdAt(document.getCreatedAt() != null ? document.getCreatedAt().toString() : null)
                .uploadedBy(document.getAppUser() != null ? document.getAppUser().getEmail() : "Unknown")
                .build();

        return ResponseEntity.ok(dto);
    }

    // Listar todos los documentos
    @GetMapping
    public ResponseEntity<List<DocumentDTO>> getAllDocuments() {
        List<DocumentDTO> documentDTOs = service.getAllDocuments()
                .stream()
                .map(doc -> DocumentDTO.builder()
                        .externalId(doc.getExternalId())
                        .fileName(doc.getFileName())
                        .createdAt(
                                doc.getCreatedAt() != null
                                        ? doc.getCreatedAt().toString()
                                        : null)
                        .uploadedBy(
                                doc.getAppUser() != null ? doc.getAppUser().getEmail() : null)
                        .build())
                .toList();

        return ResponseEntity.ok(documentDTOs);
    }
}
