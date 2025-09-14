package com.proyectofinal.proyectofinal.dto.app_user;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AppUserMinimalDTO {
    private String externalId;
    private String email;
    private LocalDateTime blockedAt;
}
