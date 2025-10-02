package com.proyectofinal.proyectofinal.service;

import com.proyectofinal.proyectofinal.model.ChatUser;
import com.proyectofinal.proyectofinal.model.Conversation;
import com.proyectofinal.proyectofinal.types.MessageOrigin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversationFlowService {
    public static final String NO_USER_RESPONSE = "Para comunicarte con el asistente virtual, por favor ingresá tu email empresarial";
    public static final String BLOCKED_USER_RESPONSE = "No estás autorizado para comunicarte con el asistente virtual";

    private final ChatUserService chatUserService;
    private final ConversationService conversationService;
    private final MessageService messageService;

    public String getResponseForMessage(String messageContent, String phoneNumber) {
        ChatUser chatUser = chatUserService.getOrCreateForPhone(messageContent, phoneNumber);
        if (chatUser == null) {
            return NO_USER_RESPONSE;
        }

        if (chatUser.isBlocked()) {
            return BLOCKED_USER_RESPONSE;
        }

        Conversation conversation = conversationService.getOrCreateActiveConversationByUser(chatUser);
        messageService.create(conversation, messageContent, MessageOrigin.USER);

        return null; //TODO: get response from AI
    }
}
