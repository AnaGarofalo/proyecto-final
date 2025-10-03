package com.proyectofinal.proyectofinal.utils;

import com.proyectofinal.proyectofinal.AbstractTest;
import com.proyectofinal.proyectofinal.model.Conversation;
import com.proyectofinal.proyectofinal.model.Message;
import com.proyectofinal.proyectofinal.types.MessageOrigin;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConversationJSONBuilderTest extends AbstractTest {
    @Test
    void buildConversationJson_emptyConversation() {
        Conversation conversation = new Conversation();
        conversation.setMessages(List.of());

        String json = ConversationJSONBuilder.buildConversationJson(conversation, "Pregunta?");
        String expected = "{\n" +
                "  \"MENSAJES_ANTERIORES\": {\n" +
                "  },\n" +
                "  \"PREGUNTA_ACTUAL\": \"Pregunta?\"\n" +
                "}";

        assertEquals(expected, json);
    }

    @Test
    void buildConversationJson_singleUserBotPair() {
        Conversation conversation = new Conversation();
        conversation.setMessages(List.of(
                Message.builder().content("Hola").origin(MessageOrigin.USER).build(),
                Message.builder().content("Hola, ¿cómo estás?").origin(MessageOrigin.BOT).build()
        ));

        String json = ConversationJSONBuilder.buildConversationJson(conversation, "Estoy bien");
        String expected = "{\n" +
                "  \"MENSAJES_ANTERIORES\": {\n" +
                "    \"usuario\": \"Hola\",\n" +
                "    \"bot\": \"Hola, ¿cómo estás?\",\n" +
                "  },\n" +
                "  \"PREGUNTA_ACTUAL\": \"Estoy bien\"\n" +
                "}";

        assertEquals(expected, json);
    }

    @Test
    void buildConversationJson_multipleMessages() {
        Conversation conversation = new Conversation();
        conversation.setMessages(List.of(
                Message.builder().content("Hola").origin(MessageOrigin.USER).build(),
                Message.builder().content("Hola!").origin(MessageOrigin.BOT).build(),
                Message.builder().content("Quiero reportar un problema").origin(MessageOrigin.USER).build(),
                Message.builder().content("Entendido, ¿cuál es el problema?").origin(MessageOrigin.BOT).build()
        ));

        String json = ConversationJSONBuilder.buildConversationJson(conversation, "Es urgente");
        String expected = "{\n" +
                "  \"MENSAJES_ANTERIORES\": {\n" +
                "    \"usuario\": \"Hola\",\n" +
                "    \"bot\": \"Hola!\",\n" +
                "    \"usuario\": \"Quiero reportar un problema\",\n" +
                "    \"bot\": \"Entendido, ¿cuál es el problema?\",\n" +
                "  },\n" +
                "  \"PREGUNTA_ACTUAL\": \"Es urgente\"\n" +
                "}";

        assertEquals(expected, json);
    }

    @Test
    void buildConversationJson_withSpecialCharacters() {
        Conversation conversation = new Conversation();
        conversation.setMessages(List.of(
                Message.builder().content("Hola \"mundo\"\nNueva línea").origin(MessageOrigin.USER).build()
        ));

        String json = ConversationJSONBuilder.buildConversationJson(conversation, "Pregunta \"especial\"?");
        String expected = "{\n" +
                "  \"MENSAJES_ANTERIORES\": {\n" +
                "    \"usuario\": \"Hola \\\"mundo\\\"\\nNueva línea\",\n" +
                "  },\n" +
                "  \"PREGUNTA_ACTUAL\": \"Pregunta \\\"especial\\\"?\"\n" +
                "}";

        assertEquals(expected, json);
    }
}
