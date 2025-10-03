package com.proyectofinal.proyectofinal.dto.app_user;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ChatResponseDTO {
    private String answer;
    private List<String> sources;
}