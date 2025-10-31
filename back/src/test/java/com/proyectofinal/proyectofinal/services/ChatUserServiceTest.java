package com.proyectofinal.proyectofinal.services;

import com.proyectofinal.proyectofinal.AbstractTest;
import com.proyectofinal.proyectofinal.exception.PFNotFoundException;
import com.proyectofinal.proyectofinal.model.ChatUser;
import com.proyectofinal.proyectofinal.service.ChatUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.*;

class ChatUserServiceTest extends AbstractTest {
    @Autowired
    ChatUserService chatUserService;

    @Value("${valid.email.domains}")
    private String validEmailDomains;

    @Test
    void getOrCreateForPhone_userExists() {
        ChatUser existingChatUser = createSampleChatUser();

        ChatUser result = chatUserService.getOrCreateForPhone(existingChatUser.getEmail(), existingChatUser.getPhoneNumber());
        assertNotNull(result);
        assertEquals(existingChatUser.getId(), result.getId());
    }

    @Test
    void getOrCreateForPhone_userDoesNotExist_validEmail() {
        String[] validEmailDomainArray = validEmailDomains.replace(" ", "").split(",");

        String email = "test@" + validEmailDomainArray[0];
        String phone = "11-1111-1112";

        ChatUser result = chatUserService.getOrCreateForPhone(email, phone);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getExternalId());
        assertEquals(email, result.getEmail());
        assertEquals(phone, result.getPhoneNumber());
        assertNull(result.getBlockedAt());
    }

    @Test
    void getOrCreateForPhone_userDoesNotExist_invalidEmail() {
        String email = "test@test.com";
        String phone = "11-1111-1112";

        ChatUser result = chatUserService.getOrCreateForPhone(email, phone);
        assertNull(result);
    }

    @Test
    void createByAdmin_success() {
        String email = "test@test.com";
        String phone = "11-1111-1112";

        ChatUser result = chatUserService.createByAdmin(email, phone);
        assertNotNull(result);
    }

    @Test
    void createByAdmin_numberUsed() {
        String email = "test@test.com";
        String phone = "11-1111-1112";

        chatUserService.createByAdmin(email, phone);

        assertThrows(IllegalArgumentException.class, () -> chatUserService.createByAdmin("test2@test.com", phone));
    }

    @Test
    void markAsBlocked_userNotFound() {
        assertThrows(PFNotFoundException.class, () -> chatUserService.markAsBlocked("fake-id"));
    }

    @Test
    void markAsBlocked_success() {
        ChatUser existingChatUser = createSampleChatUser();

        ChatUser result = chatUserService.markAsBlocked(existingChatUser.getExternalId());
        assertTrue(result.isBlocked());
        assertEquals(existingChatUser.getId(), result.getId());
    }

    @Test
    void unblock_userNotFound() {
        assertThrows(PFNotFoundException.class, () -> chatUserService.unblock("fake-id"));
    }

    @Test
    void unblock_success() {
        ChatUser existingChatUser = createSampleChatUser();

        ChatUser result = chatUserService.markAsBlocked(existingChatUser.getExternalId());
        assertTrue(result.isBlocked());
        assertEquals(existingChatUser.getId(), result.getId());

        result = chatUserService.unblock(existingChatUser.getExternalId());
        assertFalse(result.isBlocked());
        assertEquals(existingChatUser.getId(), result.getId());
    }

    @Test
    void markAsDeleted_userNotFound() {
        assertThrows(PFNotFoundException.class, () -> chatUserService.markAsDeleted("fake-id"));
    }

    @Test
    void markAsDeleted_success() {
        ChatUser existingChatUser = createSampleChatUser();

        ChatUser result = chatUserService.markAsDeleted(existingChatUser.getExternalId());
        assertNotNull(result.getDeletedAt());
        assertEquals(existingChatUser.getId(), result.getId());
    }
}
