package com.proyectofinal.proyectofinal.dto;

import com.proyectofinal.proyectofinal.model.Document;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentDTO {
    private String externalId;
    private String fileName;
    private String createdAt;
    private String uploadedBy;

    public DocumentDTO(Document doc) {
        this.externalId = doc.getExternalId();
        this.fileName = doc.getFileName();
        this.createdAt = doc.getCreatedAt() != null ? doc.getCreatedAt().toString() : null;
        this.uploadedBy = doc.getAppUser() != null ? doc.getAppUser().getEmail() : null;
    }
}
