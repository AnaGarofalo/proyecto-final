package com.proyectofinal.proyectofinal.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ChatUserDTO {
    private String externalId;
    private String email;
    private String phoneNumber;
    private LocalDateTime blockedAt;
}
