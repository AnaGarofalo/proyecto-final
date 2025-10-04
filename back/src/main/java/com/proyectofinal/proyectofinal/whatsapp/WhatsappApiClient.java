package com.proyectofinal.proyectofinal.whatsapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.proyectofinal.proyectofinal.config.WhatsappProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@RequiredArgsConstructor

public class WhatsappApiClient {
    private final RestClient whatsappRestClient;
    private final WhatsappProperties props;
    private final ObjectMapper mapper = new ObjectMapper();

    public String sendText(String toWaId, String text) {
        ObjectNode body = mapper.createObjectNode();
        body.put("messaging_product", "whatsapp");
        body.put("to", toWaId);
        body.put("type", "text");
        body.set("text", mapper.valueToTree(Map.of("preview_url", false, "body", text)));

        return whatsappRestClient.post()
                .uri("/{phoneId}/messages", props.getPhoneNumberId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(String.class);
    }
}
