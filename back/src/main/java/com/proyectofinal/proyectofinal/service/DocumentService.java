package com.proyectofinal.proyectofinal.service;

import com.proyectofinal.proyectofinal.model.AppUser;
import com.proyectofinal.proyectofinal.model.Document;
import com.proyectofinal.proyectofinal.repository.DocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class DocumentService extends AbstractService<Document, DocumentRepository> {

    public DocumentService(DocumentRepository repository) {
        super(repository, Document.class);
    }

    public Document saveFile(MultipartFile file, AppUser appUser) {
        String fileName = file.getOriginalFilename();
        log.info("Creating document for file {}", fileName);

        Document document = Document.builder()
                .fileName(fileName)
                .appUser(appUser)
                .build();

        return repository.save(document);
    }

    public List<Document> getAllDocuments() {
        return repository.findAllByDeletedAtIsNull();
    }

    public Document markAsDeleted(String externalId) {
        log.info("Deleting document for with externalId {}", externalId);

        Document document = getByExternalId(externalId);
        document.setDeletedAt(LocalDateTime.now());
        return repository.save(document);
    }
}
