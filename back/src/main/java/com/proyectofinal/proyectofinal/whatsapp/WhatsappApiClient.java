package com.proyectofinal.proyectofinal.whatsapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.proyectofinal.proyectofinal.config.WhatsappProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class WhatsappApiClient {
    private final WebClient whatsappWebClient;
    private final WhatsappProperties props;
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String PROPERTY_TYPE_TEXT = "text";

    public String sendText(String toWaId, String text) {
        ObjectNode body = mapper.createObjectNode();
        body.put("messaging_product", "whatsapp");
        body.put("to", toWaId);
        body.put("type", PROPERTY_TYPE_TEXT);
        body.set(PROPERTY_TYPE_TEXT, mapper.valueToTree(Map.of("preview_url", false, "body", text)));

        return whatsappWebClient.post()
                .uri("/{phoneId}/messages", props.getPhoneNumberId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
