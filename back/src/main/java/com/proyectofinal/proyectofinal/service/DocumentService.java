package com.proyectofinal.proyectofinal.service;

import com.proyectofinal.proyectofinal.model.AppUser;
import com.proyectofinal.proyectofinal.model.Document;
import com.proyectofinal.proyectofinal.repository.DocumentRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentService extends AbstractService<Document, DocumentRepository> {

    private final AppUserService appUserService;

    public DocumentService(DocumentRepository repository, AppUserService appUserService) {
        super(repository, Document.class);
        this.appUserService = appUserService;
    }

    public Document saveFile(MultipartFile file, AppUser appUser) {
        String fileName = file.getOriginalFilename();

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
        Document document = getByExternalId(externalId);
        document.setDeletedAt(LocalDateTime.now());
        return repository.save(document);
    }
}
