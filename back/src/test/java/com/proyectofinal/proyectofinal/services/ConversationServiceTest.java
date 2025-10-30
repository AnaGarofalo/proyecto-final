package com.proyectofinal.proyectofinal.services;

import com.proyectofinal.proyectofinal.AbstractTest;
import com.proyectofinal.proyectofinal.model.ChatUser;
import com.proyectofinal.proyectofinal.model.Conversation;
import com.proyectofinal.proyectofinal.service.ConversationService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConversationServiceTest extends AbstractTest {
    @SpyBean
    ConversationService conversationService;

    @Test
    void create_ForChatUser_chatUserIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> conversationService.create(null));
        assertTrue(exception.getMessage().contains("User is required"));
    }

    @Test
    void create_ForChatUser_success() {
        ChatUser chatUser = createSampleChatUser();

        Conversation conversation = conversationService.create(chatUser);

        assertNotNull(conversation.getId());
        assertNotNull(conversation.getExternalId());
        assertNotNull(conversation.getCreatedAt());
        assertEquals(chatUser.getId(), conversation.getChatUser().getId());
    }

    @Test
    void markAsFinished_conversationsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> conversationService.markAsFinished(null));
        assertTrue(exception.getMessage().contains("Conversation is required"));
    }

    @Test
    void markAsFinished_success() {
        Conversation originalConversation = createSampleConversation();

        Conversation finishedConversation = conversationService.markAsFinished(originalConversation);
        assertEquals(originalConversation.getId(), finishedConversation.getId());
        assertEquals(originalConversation.getLastMessageTime(), finishedConversation.getFinalizedAt());
    }

    @Test
    void getOrCreateIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> conversationService.getOrCreateActiveConversationByUser(null));
        assertTrue(exception.getMessage().contains("ChatUser is required"));

        verify(conversationService, never()).create(any(ChatUser.class));
    }

    @Test
    void getOrCreateForChatUserActiveConversationByUserId_noPreviousConversationChat() {
        ChatUser chatUser = createSampleChatUser();

        conversationService.getOrCreateActiveConversationByUser(chatUser);
        verify(conversationService).create(chatUser);
    }

    @Test
    void getOrCreateForChatUserActiveConversationByUserId_previousValidConversationChat() {
        Conversation previousConversation = createSampleConversation();
        ChatUser chatUser = previousConversation.getChatUser();
        reset(conversationService);

        Conversation conversation = conversationService.getOrCreateActiveConversationByUser(chatUser);
        assertEquals(previousConversation.getId(), conversation.getId());
        verify(conversationService, never()).create(chatUser);
    }

    @Test
    void getOrCreateForChatUserActiveConversationByUserId_previousFinalizedConversationChat() {
        Conversation previousConversation = createSampleConversation();

        conversationService.markAsFinished(previousConversation);

        ChatUser chatUser = previousConversation.getChatUser();
        reset(conversationService);

        Conversation conversation = conversationService.getOrCreateActiveConversationByUser(chatUser);
        assertNotEquals(previousConversation.getId(), conversation.getId());
        verify(conversationService).create(chatUser);
    }

    @Test
    void getOrCreateForChatUserActiveConversationByUserId_previousInvalidConversationChat() {
        Conversation previousConversation = createSampleConversation();

        previousConversation.setCreatedAt(LocalDateTime.now().minusMinutes(120L));
        conversationService.save(previousConversation);

        ChatUser chatUser = previousConversation.getChatUser();
        reset(conversationService);

        Conversation conversation = conversationService.getOrCreateActiveConversationByUser(chatUser);
        assertNotEquals(previousConversation.getId(), conversation.getId());
        verify(conversationService).create(chatUser);
        verify(conversationService).markAsFinished(previousConversation);
    }
}
