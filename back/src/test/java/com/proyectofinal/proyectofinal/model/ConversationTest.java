package com.proyectofinal.proyectofinal.model;

import com.proyectofinal.proyectofinal.AbstractTest;
import com.proyectofinal.proyectofinal.types.MessageOrigin;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ConversationTest extends AbstractTest {
    @Test
    void getLastMessageTime_noMessages_shouldReturnConversationCreatedAt() {
        Conversation conversation = createSampleConversation();

        assertTrue(conversation.getMessages().isEmpty());
        assertEquals(conversation.getCreatedAt(), conversation.getLastMessageTime());
    }

    @Test
    void getLastMessageTime_withMessages_shouldReturnLastMessageCreatedAt() {
        Conversation conversation = createSampleConversation();

        Message message1 = Message.builder()
                .conversation(conversation)
                .content("first message")
                .origin(MessageOrigin.USER)
                .build();
        message1.setCreatedAt(LocalDateTime.now().plusMinutes(10L));

        Message message2 = Message.builder()
                .conversation(conversation)
                .content("second message")
                .origin(MessageOrigin.BOT)
                .build();
        message2.setCreatedAt(LocalDateTime.now().plusMinutes(20L));

        conversation.getMessages().add(message1);
        conversation.getMessages().add(message2);
        assertFalse(conversation.getMessages().isEmpty());
        assertEquals(message2.getCreatedAt(), conversation.getLastMessageTime());
    }
}
