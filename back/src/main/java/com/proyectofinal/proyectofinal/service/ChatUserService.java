package com.proyectofinal.proyectofinal.service;

import com.proyectofinal.proyectofinal.model.ChatUser;
import com.proyectofinal.proyectofinal.repository.ChatUserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ChatUserService extends AbstractService<ChatUser, ChatUserRepository> {
    @Value("${valid.email.domain}")
    private String validEmailDomain;

    public ChatUserService(ChatUserRepository repository) {
        super(repository, ChatUser.class);
    }

    public ChatUser getOrCreateForPhone(String message, String phone) {
        Optional<ChatUser> optionalChatUser = findByPhone(phone);
        if (optionalChatUser.isPresent()) {
            return optionalChatUser.get();
        }

        return create(message, phone);
    }

    public ChatUser markAsBlocked(String externalId) {
        ChatUser chatUser = getByExternalId(externalId);

        chatUser.setBlockedAt(LocalDateTime.now());
        return repository.save(chatUser);
    }

    private ChatUser create(String email, String phone) {
        if (!isValidEmail(email)) {
            return null;
        }

        ChatUser chatUser = ChatUser.builder()
                .email(email)
                .phoneNumber(phone)
                .build();

        return save(chatUser);
    }

    private Optional<ChatUser> findByPhone(String phone) {
        return repository.findByPhoneNumberAndDeletedAtIsNull(phone);
    }

    private boolean isValidEmail(String email) {
        return !StringUtils.isEmpty(email) && email.matches("^[a-zA-Z0-9._%+-]+@" + validEmailDomain + "\\.com$");
    }
}
