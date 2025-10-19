package com.proyectofinal.proyectofinal.config;

import com.proyectofinal.proyectofinal.dto.SystemPromptDTO;
import com.proyectofinal.proyectofinal.dto.app_user.AppUserLoginDTO;
import com.proyectofinal.proyectofinal.model.AppUser;
import com.proyectofinal.proyectofinal.model.SystemPrompt;
import com.proyectofinal.proyectofinal.service.AppUserService;
import com.proyectofinal.proyectofinal.service.SystemPromptService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {

    private final AppUserService appUserService;
    private final SystemPromptService systemPromptService;

    public DataLoader(AppUserService appUserService, SystemPromptService systemPromptService) {
        this.appUserService = appUserService;
        this.systemPromptService = systemPromptService;
    }

    @Override
    public void run(String... args) throws Exception {
        createFirstUser();
        createFirstPromptData();
    }

    private void createFirstUser() {
        Optional<AppUser> opExistingUser = appUserService.findActiveByEmail("admin@root.com");
        if (opExistingUser.isEmpty()) {
            AppUserLoginDTO appUserLoginDTO = AppUserLoginDTO.builder().email("admin@root.com").password("Pass123")
                    .build();
            appUserService.create(appUserLoginDTO);
        }
    }

    private void createFirstPromptData() {
        Optional<SystemPrompt> opExistingPrompt = systemPromptService.findByTicketEmail("helpdesk@nestle.com");
        if (opExistingPrompt.isEmpty()) {
            SystemPromptDTO systemPromptDTO = SystemPromptDTO.builder()
                    .prompt("You are an AI assistant for Nestle's IT helpdesk. Your task is to help users with their technical issues related to Nestle's products and services. Always respond in a professional and courteous manner, providing clear and concise solutions.")
                    .ticketEmail("helpdesk@nestle.com")
                    .build();
            systemPromptService.create(systemPromptDTO);
        }
    }
}
