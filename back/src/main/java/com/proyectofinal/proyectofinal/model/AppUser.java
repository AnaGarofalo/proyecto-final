package com.proyectofinal.proyectofinal.model;

import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AppUser extends AbstractModel implements User {
    private String email;
    private String password; //hashed
    private LocalDateTime blockedAt;
}
