package com.proyectofinal.proyectofinal.utils;

import com.proyectofinal.proyectofinal.model.Conversation;
import com.proyectofinal.proyectofinal.model.Message;
import com.proyectofinal.proyectofinal.types.MessageOrigin;

import java.util.List;

public class ConversationJSONBuilder {
    public static String buildConversationJson(Conversation conversation, String currentQuestion) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n  \"MENSAJES_ANTERIORES\": {\n");

        List<Message> messages = conversation.getMessages();

        for (Message msg : messages) {
            if (MessageOrigin.USER.equals(msg.getOrigin())) {
                sb.append("    \"usuario\": \"").append(escapeJson(msg.getContent())).append("\",\n");
            } else {
                sb.append("    \"bot\": \"").append(escapeJson(msg.getContent())).append("\",\n");
            }
        }

        sb.append("  },\n");
        sb.append("  \"PREGUNTA_ACTUAL\": \"").append(escapeJson(currentQuestion)).append("\"\n");
        sb.append("}");

        return sb.toString();
    }

    private static String escapeJson(String text) {
        return text.replace("\"", "\\\"").replace("\n", "\\n");
    }
}
