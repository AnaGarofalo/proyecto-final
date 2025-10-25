package com.proyectofinal.proyectofinal.service;

import com.proyectofinal.proyectofinal.model.AppUser;
import com.proyectofinal.proyectofinal.model.ChatUser;
import com.proyectofinal.proyectofinal.model.Conversation;
import com.proyectofinal.proyectofinal.model.User;
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

    public Conversation getOrCreateActiveConversationByUser(User user) {
        log.info("Attempting to fetch active conversation");

        if (user == null) {
            throw new IllegalArgumentException("Error fetching active conversation: ChatUser is required");
        }

        boolean isChatUser = user instanceof ChatUser;
        log.info("Fetching active conversation for {} user {}", isChatUser ? "CHAT" : "APP", user.getExternalId());

        Optional<Conversation> previousConversation = getActiveByUserId(user.getId(), isChatUser);
        if (previousConversation.isPresent()) {
            // Si encuentra una conversación válida la devuelve
            log.info("Returning previous conversation for new message");

            previousConversation.get().getMessages().size(); // inicializa la colección
            return previousConversation.get();
        } else {
            // Sino la crea
            log.info("Creating conversation for new message");

            return create(user);
        }
    }

    public Conversation getOrCreateActiveConversationByAppUser() {
        AppUser appUser = appUserService.getFromToken();
        return getOrCreateActiveConversationByUser(appUser);
    }

    private Optional<Conversation> getActiveByUserId(Long userId, boolean isChatUser) {
        // Buscamos una conversación anterior que no haya terminado
        Optional<Conversation> previousConversation = isChatUser ? repository.findByChatUserIdAndFinalizedAtIsNull(userId) : repository.findByAppUserIdAndFinalizedAtIsNull(userId);

        // Si la conversación existe y el último mensaje es reciente, la devolvemos
        if (previousConversation.isPresent() && previousConversation.get().isRecentConversation()) {
            return previousConversation;
        } else if (previousConversation.isPresent()) {
            // Si la conversación existe pero el último mensaje tiene más de una hora, la marcamos como finalizada
            log.info("Marking previous conversation as finished (timeout) for {} user {}", isChatUser ? "CHAT" : "APP", userId);

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

    public Conversation create(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Error creating conversation: User is required");
        }

        boolean isChatUser = user instanceof ChatUser;

        log.info("Creating conversation for {} user {}", isChatUser ? "CHAT" : "APP", user.getExternalId());

        Conversation conversation = new Conversation();
        if (isChatUser) {
            conversation.setChatUser((ChatUser) user);
        } else {
            conversation.setAppUser((AppUser) user);
        }

        return repository.save(conversation);
    }
}
