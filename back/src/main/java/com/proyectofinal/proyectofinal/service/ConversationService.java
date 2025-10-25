package com.proyectofinal.proyectofinal.service;

import com.proyectofinal.proyectofinal.model.AppUser;
import com.proyectofinal.proyectofinal.model.ChatUser;
import com.proyectofinal.proyectofinal.model.Conversation;
import com.proyectofinal.proyectofinal.repository.ConversationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ConversationService extends AbstractService<Conversation, ConversationRepository> {
    private final AppUserService appUserService;
    public ConversationService(ConversationRepository repository, AppUserService appUserService) {
        super(repository, Conversation.class);
        this.appUserService = appUserService;
    }

    public Conversation getOrCreateActiveConversationByChatUser(ChatUser chatUser) {
        log.info("Attempting to fetch active conversation");

        if (chatUser == null) {
            throw new IllegalArgumentException("Error fetching active conversation: ChatUser is required");
        }

        log.info("Fetching active conversation for chat user {}", chatUser.getExternalId());

        Optional<Conversation> previousConversation = getActiveByChatUserId(chatUser.getId());
        if (previousConversation.isPresent()) {
            // Si encuentra una conversación válida la devuelve
            log.info("Returning previous conversation for new message");

            previousConversation.get().getMessages().size(); // inicializa la colección
            return previousConversation.get();
        } else {
            // Sino la crea
            log.info("Creating conversation for new message");

            return createForChatUser(chatUser);
        }
    }
    public Conversation getOrCreateActiveConversationByAppUser() {
        AppUser appUser = appUserService.getFromToken();
        return getOrCreateActiveConversationByAppUser(appUser);
    }

    public Conversation getOrCreateActiveConversationByAppUser(AppUser appUser) {
        log.info("Attempting to fetch active conversation");

        if (appUser == null) {
            throw new IllegalArgumentException("Error fetching active conversation: AppUser is required");
        }

        log.info("Fetching active conversation for APP user {}", appUser.getExternalId());

        Optional<Conversation> previousConversation = getActiveByAppUserId(appUser.getId());
        if (previousConversation.isPresent()) {
            // Si encuentra una conversación válida la devuelve
            log.info("Returning previous conversation for new message");

            previousConversation.get().getMessages().size(); // inicializa la colección
            return previousConversation.get();
        } else {
            // Sino la crea
            log.info("Creating conversation for new message");

            return createForAppUser(appUser);
        }
    }

    private Optional<Conversation> getActiveByChatUserId(Long chatUserId) {
        // Buscamos una conversación anterior que no haya terminado
        Optional<Conversation> previousConversation = repository.findByChatUserIdAndFinalizedAtIsNull(chatUserId);

        // Si la conversación existe y el último mensaje es reciente, la devolvemos
        if (previousConversation.isPresent() && previousConversation.get().isRecentConversation()) {
            return previousConversation;
        } else if (previousConversation.isPresent()) {
            // Si la conversación existe pero el último mensaje tiene más de una hora, la marcamos como finalizada
            log.info("Marking previous conversation as finished (timeout) for user {}", chatUserId);

            markAsFinished(previousConversation.get());
        }

        // Sea que la conversación era muy antigua o no encontró ninguna, devolvemos vacío
        return Optional.empty();
    }

    private Optional<Conversation> getActiveByAppUserId(Long appUserId) {
        // Buscamos una conversación anterior que no haya terminado
        Optional<Conversation> previousConversation = repository.findByAppUserIdAndFinalizedAtIsNull(appUserId);

        // Si la conversación existe y el último mensaje es reciente, la devolvemos
        if (previousConversation.isPresent() && previousConversation.get().isRecentConversation()) {
            return previousConversation;
        } else if (previousConversation.isPresent()) {
            // Si la conversación existe pero el último mensaje tiene más de una hora, la marcamos como finalizada
            log.info("Marking previous conversation as finished (timeout) for user {}", appUserId);

            markAsFinished(previousConversation.get());
        }

        // Sea que la conversación era muy antigua o no encontró ninguna, devolvemos vacío
        return Optional.empty();
    }

    public Conversation markAsFinished(Conversation conversation) {
        if (conversation == null) {
            throw new IllegalArgumentException("Error marking conversation as finished: Conversation is required");
        }

        log.info("Marking conversation {} as finished ", conversation.getExternalId());

        conversation.setFinalizedAt(conversation.getLastMessageTime());
        return repository.save(conversation);
    }

    public Conversation createForChatUser(ChatUser chatUser) {
        if (chatUser == null) {
            throw new IllegalArgumentException("Error creating conversation: ChatUser is required");
        }

        log.info("Creating conversation for CHAT user {}", chatUser.getExternalId());

        Conversation conversation = Conversation.builder()
                .chatUser(chatUser)
                .build();

        return repository.save(conversation);
    }

    public Conversation createForAppUser(AppUser appUser) {
        if (appUser == null) {
            throw new IllegalArgumentException("Error creating conversation: AppUser is required");
        }

        log.info("Creating conversation for APP user {}", appUser.getExternalId());

        Conversation conversation = Conversation.builder()
                .appUser(appUser)
                .build();

        return repository.save(conversation);
    }
}
