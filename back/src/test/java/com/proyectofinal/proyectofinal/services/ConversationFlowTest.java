package com.proyectofinal.proyectofinal.services;

import com.proyectofinal.proyectofinal.AbstractTest;
import com.proyectofinal.proyectofinal.dto.IAResponseDTO;
import com.proyectofinal.proyectofinal.model.ChatUser;
import com.proyectofinal.proyectofinal.model.Conversation;
import com.proyectofinal.proyectofinal.service.*;
import com.proyectofinal.proyectofinal.types.MessageOrigin;
import com.proyectofinal.proyectofinal.types.PremadeResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ConversationFlowTest extends AbstractTest {
    @Autowired
    ChatUserService chatUserService;

    @SpyBean
    ConversationFlowService conversationFlowService;

    @SpyBean
    ConversationService conversationService;

    @SpyBean
    MessageService messageService;

    @MockBean
    RagService ragService;

    @Value("${valid.email.domain}")
    private String validEmailDomain;


    @Test
    void getResponseForMessage_userDoesNotExistAndCouldNotBeCreated() {
        String response = conversationFlowService.getResponseForMessage("Hello there!", "11-1111-1111");

        assertEquals(PremadeResponse.NO_USER.getMessage(), response);
    }

    @Test
    void getResponseForMessage_userBlocked() {
        ChatUser chatUser = createSampleChatUser();
        chatUserService.markAsBlocked(chatUser.getExternalId());

        String response = conversationFlowService.getResponseForMessage("Some message", chatUser.getPhoneNumber());

        assertEquals(PremadeResponse.BLOCKED_USER.getMessage(), response);
    }

    @Test
    void getResponseForMessage_createsUser() {
        String response = conversationFlowService.getResponseForMessage("test@" + validEmailDomain +".com", "33333333");

        assertEquals(PremadeResponse.CREATED_USER.getMessage(), response);
    }

    @Test
    void getResponseForMessage_validUser_getsResponseFromAI() {
        ChatUser chatUser = createSampleChatUser();
        String messageContent = "Some message";
        IAResponseDTO iaResponse = new IAResponseDTO("Some response", "");

        when(ragService.ask(anyString())).thenReturn(iaResponse);
        String finalResponse = conversationFlowService.getResponseForMessage(messageContent, chatUser.getPhoneNumber());

        // Gets or creates conversation
        verify(conversationService).getOrCreateActiveConversationByUser(chatUser);

        // Saves user message
        verify(messageService).create(any(Conversation.class), eq(messageContent), eq(MessageOrigin.USER));

        // Gets response form AI
        verify(ragService).ask(anyString());

        // Saves bot message
        verify(messageService).create(any(Conversation.class), eq(iaResponse.getUserResponse()), eq(MessageOrigin.BOT));

        // Does NOT finish conversation
        verify(conversationService, never()).markAsFinished(any(Conversation.class));

        assertEquals(iaResponse.getUserResponse(), finalResponse);
    }

    @Test
    void getResponseForMessage_validUser_getsResponseFromAI_responseIncludesTicketContent() {
        ChatUser chatUser = createSampleChatUser();
        String messageContent = "Some message";
        IAResponseDTO iaResponse = new IAResponseDTO("Some response", "Some ticket content");

        when(ragService.ask(anyString())).thenReturn(iaResponse);
        String finalResponse = conversationFlowService.getResponseForMessage(messageContent, chatUser.getPhoneNumber());

        // Gets or creates conversation
        verify(conversationService).getOrCreateActiveConversationByUser(chatUser);

        // Saves user message
        verify(messageService).create(any(Conversation.class), eq(messageContent), eq(MessageOrigin.USER));

        // Gets response form AI
        verify(ragService).ask(anyString());

        // Saves bot message
        verify(messageService).create(any(Conversation.class), eq(iaResponse.getUserResponse()), eq(MessageOrigin.BOT));

        // Finishes conversation
        verify(conversationService).markAsFinished(any(Conversation.class));

        assertEquals(iaResponse.getUserResponse(), finalResponse);
    }

    @Test
    void getResponseForMessage_iaReturnsEmptyResponse() {
        ChatUser chatUser = createSampleChatUser();
        String messageContent = "Some message";
        IAResponseDTO iaResponse = new IAResponseDTO("", "");

        when(ragService.ask(anyString())).thenReturn(iaResponse);
        String finalResponse = conversationFlowService.getResponseForMessage(messageContent, chatUser.getPhoneNumber());

        // Gets or creates conversation
        verify(conversationService).getOrCreateActiveConversationByUser(chatUser);

        // Saves user message
        verify(messageService).create(any(Conversation.class), eq(messageContent), eq(MessageOrigin.USER));

        // Gets response form AI
        verify(ragService).ask(anyString());

        // Saves bot message
        verify(messageService).create(any(Conversation.class), eq(PremadeResponse.ERROR.getMessage()), eq(MessageOrigin.BOT));

        assertEquals(PremadeResponse.ERROR.getMessage(), finalResponse);
    }

    @Test
    void getResponseForMessage_ragServiceFails() {
        ChatUser chatUser = createSampleChatUser();
        String messageContent = "Some message";

        when(ragService.ask(anyString())).thenThrow(RuntimeException.class);
        String finalResponse = conversationFlowService.getResponseForMessage(messageContent, chatUser.getPhoneNumber());

        // Gets or creates conversation
        verify(conversationService).getOrCreateActiveConversationByUser(chatUser);

        // Saves user message
        verify(messageService).create(any(Conversation.class), eq(messageContent), eq(MessageOrigin.USER));

        // Gets response form AI
        verify(ragService).ask(anyString());

        // Saves bot message
        verify(messageService).create(any(Conversation.class), eq(PremadeResponse.ERROR.getMessage()), eq(MessageOrigin.BOT));

        assertEquals(PremadeResponse.ERROR.getMessage(), finalResponse);
    }
}
