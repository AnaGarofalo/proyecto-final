package com.proyectofinal.proyectofinal.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectofinal.proyectofinal.dto.IAResponseDTO;
import com.proyectofinal.proyectofinal.types.IAResponseKeys;

public class IAResponseParser {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static IAResponseDTO parse(String json) {
        if (json == null || json.isBlank()) {
            return new IAResponseDTO("", "");
        }

        try {
            JsonNode root = objectMapper.readTree(json);

            String userResponse = getValueOrEmpty(root, IAResponseKeys.USER_RESPONSE);
            String ticketContent = getValueOrEmpty(root, IAResponseKeys.TICKET_CONTENT);

            return new IAResponseDTO(userResponse, ticketContent);

        } catch (Exception e) {
            // JSON mal formado, devolvemos objeto vacío
            return new IAResponseDTO("", "");
        }
    }

    private static String getValueOrEmpty(JsonNode root, IAResponseKeys key) {
        JsonNode node = root.get(key.name());
        return node != null && !node.isNull() ? node.asText() : "";
    }
}