package com.proyectofinal.proyectofinal.model;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SystemPrompt extends AbstractModel {
    private String prompt;
    private String ticketEmail;
}
