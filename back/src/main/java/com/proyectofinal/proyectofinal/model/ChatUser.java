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
public class ChatUser extends AbstractModel implements User {
    private String email;
    private String phoneNumber;
    private LocalDateTime blockedAt;

    public boolean isBlocked () {
        return blockedAt != null;
    }
}
