package com.proyectofinal.proyectofinal.service;

import com.proyectofinal.proyectofinal.dto.IAResponseDTO;
import com.proyectofinal.proyectofinal.dto.UserCheckResponseDTO;
import com.proyectofinal.proyectofinal.model.ChatUser;
import com.proyectofinal.proyectofinal.model.Conversation;
import com.proyectofinal.proyectofinal.types.MessageOrigin;
import com.proyectofinal.proyectofinal.types.PremadeResponse;
import com.proyectofinal.proyectofinal.utils.ConversationJSONBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConversationFlowService {
    private final ChatUserService chatUserService;
    private final ConversationService conversationService;
    private final MessageService messageService;
    private final RagService ragService;

    public String getResponseForMessage(String messageContent, String phoneNumber) {
        ChatUser chatUser;
        UserCheckResponseDTO userCheckResponseDTO = resolveChatUser(messageContent, phoneNumber);

        if (userCheckResponseDTO.getPremadeResponse() != null) {
            return userCheckResponseDTO.getPremadeResponse().getMessage();
        }
        chatUser = userCheckResponseDTO.getChatUser();

        Conversation conversation = conversationService.getOrCreateActiveConversationByUser(chatUser);
        messageService.create(conversation, messageContent, MessageOrigin.USER);

        IAResponseDTO response = getIAResponse(conversation, messageContent);
        messageService.create(conversation, response.getUserResponse(), MessageOrigin.BOT);

        if (!StringUtils.isEmpty(response.getTicketContent())) {
            conversationService.markAsFinished(conversation);
        }

        return response.getUserResponse();
    }

    private UserCheckResponseDTO resolveChatUser(String messageContent, String phoneNumber) {
        Optional<ChatUser> existingUser = chatUserService.findByPhone(phoneNumber);

        if (existingUser.isPresent()) {
            ChatUser chatUser = existingUser.get();
            if (chatUser.isBlocked()) {
                return new UserCheckResponseDTO(PremadeResponse.BLOCKED_USER);
            }
            return new UserCheckResponseDTO(chatUser);
        }

        ChatUser newUser = chatUserService.getOrCreateForPhone(messageContent, phoneNumber);
        if (newUser == null) {
            return new UserCheckResponseDTO(PremadeResponse.NO_USER);
        } else {
            return new UserCheckResponseDTO(PremadeResponse.CREATED_USER);
        }
    }

    private IAResponseDTO getIAResponse(Conversation conversation, String messageContent) {
        try {
            String formattedQuestionForIA = ConversationJSONBuilder.buildConversationJson(conversation, messageContent);
            IAResponseDTO response = ragService.ask(formattedQuestionForIA);

            if (StringUtils.isEmpty(response.getUserResponse())) {
                response.setUserResponse(PremadeResponse.ERROR.getMessage());
            }
            return response;
        } catch (Exception e) {
            return new IAResponseDTO(PremadeResponse.ERROR.getMessage(), "");
        }
    }
}
