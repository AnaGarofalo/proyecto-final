package com.proyectofinal.proyectofinal.services;

import com.proyectofinal.proyectofinal.AbstractTest;
import com.proyectofinal.proyectofinal.dto.app_user.AppUserLoginDTO;
import com.proyectofinal.proyectofinal.exception.PFNotFoundException;
import com.proyectofinal.proyectofinal.model.AppUser;
import com.proyectofinal.proyectofinal.service.AppUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AppUserServiceTest extends AbstractTest {
    @Autowired
    AppUserService appUserService;

    AppUserLoginDTO appUserLoginDTO;
    String email = "test@mail.com";

    @BeforeEach
    void setup() {
        appUserLoginDTO = getLoginDTOSample();
    }

    @Test
    void create_noExistingUser() {
        AppUser appUser = appUserService.create(appUserLoginDTO);

        assertNotNull(appUser);
        assertNotNull(appUser.getId());
        assertNotNull(appUser.getExternalId());
        assertNotNull(appUser.getCreatedAt());
        assertNotNull(appUser.getUpdatedAt());
        assertNull(appUser.getBlockedAt());
        assertEquals(appUserLoginDTO.getEmail(), appUser.getEmail());
        assertNotEquals(appUserLoginDTO.getPassword(), appUser.getPassword());

        Optional<AppUser> appUserFromDB = appUserService.findByExternalId(appUser.getExternalId());
        assertTrue(appUserFromDB.isPresent());
    }

    @Test
    void create_existingUser() {
        // Creates the first user
        appUserService.create(appUserLoginDTO);

        // Attempts to create second user with same email
        assertThrows(IllegalArgumentException.class, () -> appUserService.create(appUserLoginDTO));
    }

    @Test
    void getByEmail_existingUser() {
        AppUser created = getBaseUser();

        AppUser fetched = appUserService.getByEmail(created.getEmail());

        assertEquals(created.getId(), fetched.getId());
    }

    @Test
    void getByEmail_noExistingUser() {
        assertThrows(PFNotFoundException.class, () -> appUserService.getByEmail(email));
    }

    private AppUserLoginDTO getLoginDTOSample() {
        return AppUserLoginDTO.builder()
                .email(email)
                .password("password")
                .build();
    }
}
