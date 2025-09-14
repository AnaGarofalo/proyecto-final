package com.proyectofinal.proyectofinal.config;

import com.proyectofinal.proyectofinal.dto.app_user.AppUserCreationDTO;
import com.proyectofinal.proyectofinal.model.AppUser;
import com.proyectofinal.proyectofinal.service.AppUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {

    private final AppUserService appUserService;

    public DataLoader(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Override
    public void run(String... args) throws Exception {
        createFirstUser();
    }

    private void createFirstUser() {
        Optional<AppUser> opExistingUser = appUserService.findActiveByEmail("admin@root.com");
        if (opExistingUser.isEmpty()) {
            AppUserCreationDTO appUserCreationDTO = AppUserCreationDTO.builder().email("admin@root.com").password("pass").build();
            appUserService.create(appUserCreationDTO);
        }
    }
}
