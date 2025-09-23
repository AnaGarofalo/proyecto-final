package com.proyectofinal.proyectofinal.services;

import com.proyectofinal.proyectofinal.AbstractTest;
import com.proyectofinal.proyectofinal.model.ChatUser;
import com.proyectofinal.proyectofinal.service.ChatUserService;
import com.proyectofinal.proyectofinal.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageServiceTest extends AbstractTest {
    @Autowired
    ChatUserService chatUserService;

    @Autowired
    MessageService messageService;

    @Test
    void getResponseForMessage_userDoesNotExistAndCouldNotBeCreated() {
        String response = messageService.getResponseForMessage("Hello there!", "11-1111-1111");

        assertEquals(MessageService.NO_USER_RESPONSE, response);
    }

    @Test
    void getResponseForMessage_userBlocked() {
        ChatUser chatUser = createSampleChatUser();
        chatUserService.markAsBlocked(chatUser.getExternalId());

        String response = messageService.getResponseForMessage("Some message", chatUser.getPhoneNumber());

        assertEquals(MessageService.BLOCKED_USER_RESPONSE, response);
    }
}
