package com.proyectofinal.proyectofinal.service;

import com.proyectofinal.proyectofinal.model.Document;
import com.proyectofinal.proyectofinal.repository.DocumentRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentService extends AbstractService<Document, DocumentRepository> {

    public DocumentService(DocumentRepository repository) {
        super(repository, Document.class);
    }

    // Método para guardar un archivo en la base de datos
    public Document saveFile(MultipartFile file) {
        String fileName = file.getOriginalFilename(); // Obtener el nombre del archivo

        Document document = Document.builder()
                .fileName(fileName)
                .build();

        return repository.save(document); // Guardar el documento en la base de datos
    }

    // Método para obtener todos los documentos
    public List<Document> getAllDocuments() {
        return repository.findAll();
    }

}
