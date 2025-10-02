package com.proyectofinal.proyectofinal.services;

import com.proyectofinal.proyectofinal.AbstractTest;
import com.proyectofinal.proyectofinal.model.ChatUser;
import com.proyectofinal.proyectofinal.model.Conversation;
import com.proyectofinal.proyectofinal.service.ChatUserService;
import com.proyectofinal.proyectofinal.service.ConversationFlowService;
import com.proyectofinal.proyectofinal.service.ConversationService;
import com.proyectofinal.proyectofinal.service.MessageService;
import com.proyectofinal.proyectofinal.types.MessageOrigin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class ConversationFlowTest extends AbstractTest {
    @Autowired
    ChatUserService chatUserService;

    @SpyBean
    ConversationFlowService conversationFlowService;

    @SpyBean
    ConversationService conversationService;

    @SpyBean
    MessageService messageService;


    @Test
    void getResponseForMessage_userDoesNotExistAndCouldNotBeCreated() {
        String response = conversationFlowService.getResponseForMessage("Hello there!", "11-1111-1111");

        assertEquals(ConversationFlowService.NO_USER_RESPONSE, response);
    }

    @Test
    void getResponseForMessage_userBlocked() {
        ChatUser chatUser = createSampleChatUser();
        chatUserService.markAsBlocked(chatUser.getExternalId());

        String response = conversationFlowService.getResponseForMessage("Some message", chatUser.getPhoneNumber());

        assertEquals(ConversationFlowService.BLOCKED_USER_RESPONSE, response);
    }

    @Test
    void getResponseForMessage_validUser_createsMessageAndConversation() {
        ChatUser chatUser = createSampleChatUser();
        String messageContent = "Some message";
        conversationFlowService.getResponseForMessage(messageContent, chatUser.getPhoneNumber());

        verify(conversationService).getOrCreateActiveConversationByUser(chatUser);
        verify(messageService).create(any(Conversation.class), eq(messageContent), eq(MessageOrigin.USER));
    }
}
