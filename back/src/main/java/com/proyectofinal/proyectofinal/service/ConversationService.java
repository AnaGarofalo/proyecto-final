package com.proyectofinal.proyectofinal.service;

import com.proyectofinal.proyectofinal.model.ChatUser;
import com.proyectofinal.proyectofinal.model.Conversation;
import com.proyectofinal.proyectofinal.repository.ConversationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConversationService extends AbstractService<Conversation, ConversationRepository> {
    public ConversationService(ConversationRepository repository) {
        super(repository, Conversation.class);
    }

    public Conversation getOrCreateActiveConversationByUser(ChatUser chatUser) {
        if (chatUser == null) {
            throw new IllegalArgumentException("Error fetching active conversation: ChatUser is required");
        }

        Optional<Conversation> previousConversation = getActiveByUserId(chatUser.getId());
        if (previousConversation.isPresent()) {
            // Si encuentra una conversación válida la devuelve
            previousConversation.get().getMessages().size(); // inicializa la colección
            return previousConversation.get();
        } else {
            // Sino la crea
            return create(chatUser);
        }
    }

    private Optional<Conversation> getActiveByUserId(Long userId) {
        // Buscamos una conversación anterior que no haya terminado
        Optional<Conversation> previousConversation = repository.findByChatUserIdAndFinalizedAtIsNull(userId);

        // Si la conversación existe y el último mensaje es reciente, la devolvemos
        if (previousConversation.isPresent() && previousConversation.get().isRecentConversation()) {
            return previousConversation;
        } else if (previousConversation.isPresent()) {
            // Si la conversación existe pero el último mensaje tiene más de una hora, la marcamos como finalizada
            markAsFinished(previousConversation.get());
        }

        // Sea que la conversación era muy antigua o no encontró ninguna, devolvemos vacío
        return Optional.empty();
    }

    public Conversation markAsFinished(Conversation conversation) {
        if (conversation == null) {
            throw new IllegalArgumentException("Error marking conversation as finished: Conversation is required");
        }

        conversation.setFinalizedAt(conversation.getLastMessageTime());
        return repository.save(conversation);
    }

    public Conversation create(ChatUser chatUser) {
        if (chatUser == null) {
            throw new IllegalArgumentException("Error creating conversation: ChatUser is required");
        }
        Conversation conversation = Conversation.builder()
                .chatUser(chatUser)
                .build();

        return repository.save(conversation);
    }
}
