package com.proyectofinal.proyectofinal.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectofinal.proyectofinal.config.WhatsappProperties;
import com.proyectofinal.proyectofinal.utils.SignatureUtil;
import com.proyectofinal.proyectofinal.whatsapp.WhatsappApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${whatsapp.webhook.path}")
public class WhatsappWebhookController {
    private final WhatsappProperties props;
    private final WhatsappApiClient client;
    private final ConversationFlowService conversationFlowService;
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String FORBIDDEN_MESSAGE = "INVALID ACCESS";

    @GetMapping
    public ResponseEntity<String> verify(
            @RequestParam(name = "hub.mode", required = false) String mode,
            @RequestParam(name = "hub.verify_token", required = false) String token,
            @RequestParam(name = "hub.challenge", required = false) String challenge) {
        if ("subscribe".equals(mode) && props.getVerifyToken().equals(token)) {
            return ResponseEntity.ok(challenge);
        }
        return ResponseEntity.status(403).body(FORBIDDEN_MESSAGE);
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
            JsonNode messages = root.path("entry").get(0).path("changes").get(0).path("value").path("messages");

            if (messages.isArray() && !messages.isEmpty()) {
                for (JsonNode msg : messages) {
                    String from = msg.path("from").asText();
                    String text = extractUserText(msg);

                    if (text == null || text.isBlank()) {
                        String answer = "Recibido ✅";
                        log.info("WA IN from={} text='{}' -> replying='{}'", from, text, answer);
                        client.sendText(from, answer);
                    } else {
                        try {
                            String iaResponse = conversationFlowService.getResponseForMessage(text, from);
                            log.info("WA IN from={} text='{}' -> IA response='{}'", from, text, iaResponse);
                            client.sendText(from, iaResponse);
                        } catch (Exception e) {
                            log.error("Error obteniendo respuesta IA para from={} text='{}'", from, text, e);
                            client.sendText(from, "Lo siento, ha ocurrido un error. Inténtalo más tarde.");
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error procesando webhook WhatsApp", e);
        }

        return ResponseEntity.ok().build();
    }

    private String extractUserText(JsonNode msg) {
        String type = msg.path("type").asText("");
        switch (type) {
            case "text":
                return msg.path("text").path("body").asText("");
            case "button":
                return msg.path("button").path("text").asText("");
            case "interactive":
                JsonNode i = msg.path("interactive");
                if (i.has("button_reply")) return i.path("button_reply").path("title").asText("");
                if (i.has("list_reply"))   return i.path("list_reply").path("title").asText("");
                return "";
            default:
                return "";
        }
    }
}
