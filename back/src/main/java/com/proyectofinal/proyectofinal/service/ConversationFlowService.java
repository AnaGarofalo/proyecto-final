package com.proyectofinal.proyectofinal.service;

import com.proyectofinal.proyectofinal.dto.IAResponseDTO;
import com.proyectofinal.proyectofinal.dto.MessageDTO;
import com.proyectofinal.proyectofinal.dto.UserCheckResponseDTO;
import com.proyectofinal.proyectofinal.model.AppUser;
import com.proyectofinal.proyectofinal.model.ChatUser;
import com.proyectofinal.proyectofinal.model.Conversation;
import com.proyectofinal.proyectofinal.types.MessageOrigin;
import com.proyectofinal.proyectofinal.types.PremadeResponse;
import com.proyectofinal.proyectofinal.utils.ConversationJSONBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationFlowService {
    private final ChatUserService chatUserService;
    private final ConversationService conversationService;
    private final MessageService messageService;
    private final RagService ragService;
    private final SystemPromptService systemPromptService;
    private final AppUserService appUserService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private TicketService ticketService;

    public MessageDTO getResponseForAppUserMessage(String messageContent) {
        log.info("Received message from APP user");

        AppUser appUser = appUserService.getFromToken();
        Conversation conversation = conversationService.getOrCreateActiveConversationByAppUser(appUser);
        messageService.create(conversation, messageContent, MessageOrigin.USER);

        log.info("Requesting response from IA");
        IAResponseDTO response = getIAResponse(conversation, messageContent);
        messageService.create(conversation, response.getUserResponse(), MessageOrigin.BOT);

        if (!StringUtils.isEmpty(response.getTicketContent())) {
            log.info("Received ticket creation request from IA: finishing conversation");
            conversationService.markAsFinished(conversation);
        }

        return new MessageDTO(response.getUserResponse(), MessageOrigin.BOT);
    }

    public String getResponseForMessage(String messageContent, String phoneNumber) {
        log.info("Received message, processing response");
        ChatUser chatUser;
        UserCheckResponseDTO userCheckResponseDTO = resolveChatUser(messageContent, phoneNumber);

        if (userCheckResponseDTO.getPremadeResponse() != null) {
            log.info("Returning premade response");
            return userCheckResponseDTO.getPremadeResponse().getMessage();
        }
        chatUser = userCheckResponseDTO.getChatUser();

        Conversation conversation = conversationService.getOrCreateActiveConversationByChatUser(chatUser);
        messageService.create(conversation, messageContent, MessageOrigin.USER);

        log.info("Requesting response from IA");
        IAResponseDTO response = getIAResponse(conversation, messageContent);
        messageService.create(conversation, response.getUserResponse(), MessageOrigin.BOT);

        if (!StringUtils.isEmpty(response.getTicketContent())) {
            log.info("Received ticket creation request from IA: finishing conversation");
            conversationService.markAsFinished(conversation);

            try {
                ticketService.create(chatUser, response.getTicketContent(), conversation);
                emailSenderService.sendEmail(systemPromptService.getLatest().getTicketEmail(), "Soporte - Ticket Nuevo", response.getTicketContent());
            } catch (Exception e) {
                log.error("Error creating ticket", e);
            }
        }

        return response.getUserResponse();
    }

    private UserCheckResponseDTO resolveChatUser(String messageContent, String phoneNumber) {
        Optional<ChatUser> existingUser = chatUserService.findByPhone(phoneNumber);

        if (existingUser.isPresent()) {
            ChatUser chatUser = existingUser.get();
            if (chatUser.isBlocked()) {
                log.info("User {} was blocked", chatUser.getExternalId());
                return new UserCheckResponseDTO(PremadeResponse.BLOCKED_USER);
            }
            return new UserCheckResponseDTO(chatUser);
        }

        ChatUser newUser = chatUserService.getOrCreateForPhone(messageContent, phoneNumber);
        if (newUser == null) {
            log.info("User couldn't be created");
            return new UserCheckResponseDTO(PremadeResponse.NO_USER);
        } else {
            log.info("User created");
            return new UserCheckResponseDTO(PremadeResponse.CREATED_USER);
        }
    }

    private IAResponseDTO getIAResponse(Conversation conversation, String messageContent) {
        try {
            String formattedQuestionForIA = ConversationJSONBuilder.buildConversationJson(conversation, messageContent);
            IAResponseDTO response = ragService.ask(formattedQuestionForIA);

            if (StringUtils.isEmpty(response.getUserResponse())) {
                log.warn("IA returned empty message: returning error message");

                response.setUserResponse(PremadeResponse.ERROR.getMessage());
            }
            return response;
        } catch (Exception e) {
            log.error("IA failed: returning error message");

            return new IAResponseDTO(PremadeResponse.ERROR.getMessage(), "");
        }
    }
}
