package com.proyectofinal.proyectofinal;

import com.proyectofinal.proyectofinal.model.AppUser;
import com.proyectofinal.proyectofinal.model.ChatUser;
import com.proyectofinal.proyectofinal.service.AppUserService;
import com.proyectofinal.proyectofinal.service.ChatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public abstract class AbstractTest {
    @Autowired
    private AppUserService appUserService;

    @Autowired
    private ChatUserService chatUserService;

    public AppUser getBaseUser() {
        return appUserService.getByEmail("admin@root.com");
    }

    protected ChatUser createSampleChatUser() {
        ChatUser existingChatUser = ChatUser.builder()
                .phoneNumber("11-1111-1111")
                .email("test@email.com")
                .build();

        return chatUserService.save(existingChatUser);
    }
}
