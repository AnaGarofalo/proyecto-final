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
                return ResponseEntity.ok(new DocumentDTO(document));
        }

        @GetMapping
        public ResponseEntity<List<DocumentDTO>> getAllDocuments() {
                List<DocumentDTO> documentDTOs = service.getAllDocuments()
                                .stream()
                                .map(DocumentDTO::new)
                                .toList();

                return ResponseEntity.ok(documentDTOs);
        }
}
