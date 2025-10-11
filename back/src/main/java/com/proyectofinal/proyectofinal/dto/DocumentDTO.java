package com.proyectofinal.proyectofinal.dto;

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
}
