package com.proyectofinal.proyectofinal.dto.app_user;

import lombok.Data;
import java.util.List;

@Data
public class ChatResponseDTO {
    private String answer;
    private List<String> sources;
}