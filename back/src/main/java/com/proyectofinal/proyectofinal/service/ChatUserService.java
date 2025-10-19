package com.proyectofinal.proyectofinal.service;

import com.proyectofinal.proyectofinal.model.ChatUser;
import com.proyectofinal.proyectofinal.repository.ChatUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ChatUserService extends AbstractService<ChatUser, ChatUserRepository> {
    @Value("${valid.email.domain}")
    private String validEmailDomain;

    public ChatUserService(ChatUserRepository repository) {
        super(repository, ChatUser.class);
    }

    public ChatUser getOrCreateForPhone(String message, String phone) {
        log.info("Attempting to create user for email {} and phone {}", message, phone);
        Optional<ChatUser> optionalChatUser = findByPhone(phone);
        if (optionalChatUser.isPresent()) {
            log.info("User for email {} and phone {} already existed", message, phone);
            return optionalChatUser.get();
        }

        return create(message, phone);
    }

    public ChatUser markAsBlocked(String externalId) {
        log.info("Blocking user with externalId {}", externalId);
        ChatUser chatUser = getByExternalId(externalId);

        chatUser.setBlockedAt(LocalDateTime.now());
        return repository.save(chatUser);
    }

    public ChatUser unblock(String externalId) {
        log.info("Unblocking user with externalId {}", externalId);
        ChatUser chatUser = getByExternalId(externalId);

        chatUser.setBlockedAt(null);
        return repository.save(chatUser);
    }

    public ChatUser markAsDeleted(String externalId) {
        log.info("Deleting user with externalId {}", externalId);
        ChatUser chatUser = getByExternalId(externalId);

        chatUser.setDeletedAt(LocalDateTime.now());
        return repository.save(chatUser);
    }

    public ChatUser createByAdmin(String email, String phone) {
        log.info("Admin creating user for email {} and phone {}", email, phone);

        Optional<ChatUser> optionalChatUser = findByPhone(phone);
        if (optionalChatUser.isPresent()) {
            throw new IllegalArgumentException("Phone already in use");
        }
        ChatUser chatUser = ChatUser.builder()
                .email(email)
                .phoneNumber(phone)
                .build();

        return save(chatUser);
    }

    private ChatUser create(String email, String phone) {
        log.info("Creating user for email {} and phone {}", email, phone);

        if (!isValidEmail(email)) {
            log.warn("Email invalid {}", email);
            return null;
        }

        ChatUser chatUser = ChatUser.builder()
                .email(email)
                .phoneNumber(phone)
                .build();

        return save(chatUser);
    }

    public Optional<ChatUser> findByPhone(String phone) {
        return repository.findByPhoneNumberAndDeletedAtIsNull(phone);
    }

    private boolean isValidEmail(String email) {
        return !StringUtils.isEmpty(email) && email.matches("^[a-zA-Z0-9._%+-]+@" + validEmailDomain + "\\.com$");
    }

    public List<ChatUser> getAllActive() {
        return repository.findByDeletedAtIsNullOrderByCreatedAtDesc();
    }
}
