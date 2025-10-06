package com.proyectofinal.proyectofinal.controller;

import com.proyectofinal.proyectofinal.model.Document;
import com.proyectofinal.proyectofinal.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("documents")
public class DocumentController {

    private final DocumentService service;

    private static final String FILE = "file";

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    // Subida de un archivo a la base de datos
    @PostMapping("/upload")
    public ResponseEntity<Document> upload(@RequestParam(FILE) MultipartFile file) throws IOException {
        Document document = service.saveFile(file); // Guardar el archivo en la base de datos
        return ResponseEntity.ok(document);
    }

}
