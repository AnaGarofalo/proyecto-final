package com.proyectofinal.proyectofinal.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser extends AbstractModel{
    private String email;
    private String password; //hashed
    private LocalDateTime deletedAt;
}
