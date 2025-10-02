package com.proyectofinal.proyectofinal.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectofinal.proyectofinal.config.WhatsappProperties;
import com.proyectofinal.proyectofinal.service.MessageService;
import com.proyectofinal.proyectofinal.utils.SignatureUtil;
import com.proyectofinal.proyectofinal.whatsapp.WhatsappApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${whatsapp.webhook.path:/webhooks/whatsapp}")
public class WhatsappWebhookController {
    private final WhatsappProperties props;
    private final WhatsappApiClient client;
    private final MessageService messageService;
    private final ObjectMapper mapper = new ObjectMapper();

    @GetMapping
    public ResponseEntity<String> verify(
            @RequestParam(name = "hub.mode", required = false) String mode,
            @RequestParam(name = "hub.verify_token", required = false) String token,
            @RequestParam(name = "hub.challenge", required = false) String challenge) {
        if ("subscribe".equals(mode) && props.getVerifyToken().equals(token)) {
            return ResponseEntity.ok(challenge);
        }
        return ResponseEntity.status(403).body("Forbidden");
    }

    @PostMapping
    public ResponseEntity<Void> receive(
            @RequestHeader(name = "X-Hub-Signature-256", required = false) String signature,
            @RequestBody String payload) {

        if (!SignatureUtil.isValidHmacSha256(signature, payload, props.getApp().getSecret())) {
            return ResponseEntity.status(401).build();
        }

        try {
            JsonNode root = mapper.readTree(payload);
            JsonNode changes = root.path("entry").get(0).path("changes").get(0);
            JsonNode messages = changes.path("value").path("messages");
            if (messages.isArray() && messages.size() > 0) {
                JsonNode msg = messages.get(0);
                String from = msg.path("from").asText();
                String text = msg.path("text").path("body").asText("");

                if (!text.isBlank()) {
                    String answer = messageService.getResponseForMessage(text, from);
                    if (answer == null || answer.isBlank()) {
                        answer = "Recibido ✅";
                    }
                    client.sendText(from, answer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }
}
