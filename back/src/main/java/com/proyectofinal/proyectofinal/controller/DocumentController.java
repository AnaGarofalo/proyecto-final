package com.proyectofinal.proyectofinal.controller;

import com.proyectofinal.proyectofinal.dto.app_user.DocumentDTO;
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

    // Subida de un archivo a la base de datos
    @PostMapping("/upload")
    public ResponseEntity<DocumentDTO> upload(@RequestParam("file") MultipartFile file) throws IOException {
        Document document = service.saveFile(file);

        DocumentDTO dto = DocumentDTO.builder()
                .fileName(document.getFileName())
                .createdAt(
                        document.getCreatedAt() != null
                                ? document.getCreatedAt().toString()
                                : null)
                .build();

        return ResponseEntity.ok(dto);
    }

    // listar todos los documentos
    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        List<Document> documents = service.getAllDocuments();
        return ResponseEntity.ok(documents);
    }

}
