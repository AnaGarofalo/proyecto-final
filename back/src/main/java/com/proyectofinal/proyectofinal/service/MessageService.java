package com.proyectofinal.proyectofinal.service;

import com.proyectofinal.proyectofinal.model.Conversation;
import com.proyectofinal.proyectofinal.model.Message;
import com.proyectofinal.proyectofinal.repository.MessageRepository;
import com.proyectofinal.proyectofinal.types.MessageOrigin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class MessageService extends AbstractService<Message, MessageRepository>{
    public MessageService(MessageRepository repository) {
        super(repository, Message.class);
    }

    public Message create(Conversation conversation, String content, MessageOrigin origin) {
        checkElementsValidity(conversation, content, origin);

        Message message = Message.builder()
                .conversation(conversation)
                .content(content)
                .origin(origin)
                .build();

        return repository.save(message);
    }

    private void checkElementsValidity(Conversation conversation, String content, MessageOrigin origin) {
        if (conversation == null) {
            throw new IllegalArgumentException("Error creating user: Conversation is required");
        } else if (StringUtils.isEmpty(content)) {
            throw new IllegalArgumentException("Error creating user: Message is empty");
        } else if (origin == null) {
            throw new IllegalArgumentException("Error creating user: Origin is required");
        }
    }
}