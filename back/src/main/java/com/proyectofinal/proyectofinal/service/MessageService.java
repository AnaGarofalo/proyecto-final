package com.proyectofinal.proyectofinal.service;

import com.proyectofinal.proyectofinal.model.ChatUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    public static final String NO_USER_RESPONSE = "Para comunicarte con el asistente virtual, por favor ingresá tu email empresarial";
    public static final String BLOCKED_USER_RESPONSE = "No estás autorizado para comunicarte con el asistente virtual";

    private final ChatUserService chatUserService;

    public String getResponseForMessage(String message, String phoneNumber) {
        ChatUser chatUser = chatUserService.getOrCreateForPhone(message, phoneNumber);
        if (chatUser == null) {
            return NO_USER_RESPONSE;
        }

        if (chatUser.isBlocked()) {
            return BLOCKED_USER_RESPONSE;
        }

        return null; //TODO: get response from AI
    }
}
