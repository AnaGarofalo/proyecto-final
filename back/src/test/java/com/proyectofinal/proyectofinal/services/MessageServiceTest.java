package com.proyectofinal.proyectofinal.services;

import com.proyectofinal.proyectofinal.AbstractTest;
import com.proyectofinal.proyectofinal.model.Conversation;
import com.proyectofinal.proyectofinal.model.Message;
import com.proyectofinal.proyectofinal.service.MessageService;
import com.proyectofinal.proyectofinal.types.MessageOrigin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class MessageServiceTest extends AbstractTest {
    @Autowired
    MessageService messageService;

    @Test
    void create_conversationIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> messageService.create(null, "test content", MessageOrigin.USER));
        assertTrue(exception.getMessage().contains("Conversation is required"));
    }

    @Test
    void create_contentIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> messageService.create(createSampleConversation(), null, MessageOrigin.USER));
        assertTrue(exception.getMessage().contains("Message is empty"));
    }

    @Test
    void create_contentIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> messageService.create(createSampleConversation(), "", MessageOrigin.USER));
        assertTrue(exception.getMessage().contains("Message is empty"));
    }

    @Test
    void create_originNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> messageService.create(createSampleConversation(), "test content", null));
        assertTrue(exception.getMessage().contains("Origin is required"));
    }

    @Test
    void create_success() {
        Conversation conversation = createSampleConversation();
        String content = "test content";
        Message message = messageService.create(conversation, content, MessageOrigin.BOT);

        assertNotNull(message.getId());
        assertNotNull(message.getExternalId());
        assertNotNull(message.getCreatedAt());
        assertEquals(conversation.getId(), message.getConversation().getId());
        assertEquals(content, message.getContent());
        assertEquals(MessageOrigin.BOT, message.getOrigin());
    }
}
