package com.proyectofinal.proyectofinal.service;

import com.proyectofinal.proyectofinal.model.AppUser;
import com.proyectofinal.proyectofinal.model.Document;
import com.proyectofinal.proyectofinal.repository.DocumentRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class DocumentService extends AbstractService<Document, DocumentRepository> {

    private final AppUserService appUserService;

    public DocumentService(DocumentRepository repository, AppUserService appUserService) {
        super(repository, Document.class);
        this.appUserService = appUserService;
    }

    public Document saveFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();

        // Obtener el email del usuario autenticado desde el contexto de seguridad
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Buscar el usuario activo por email
        AppUser user = appUserService.findActiveByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        Document document = Document.builder()
                .fileName(fileName)
                .appUser(user)
                .build();

        return repository.save(document);
    }

    public List<Document> getAllDocuments() {
        return repository.findAll();
    }
}
