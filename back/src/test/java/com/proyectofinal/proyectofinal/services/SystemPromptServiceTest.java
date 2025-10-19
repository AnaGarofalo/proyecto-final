package com.proyectofinal.proyectofinal.services;

import com.proyectofinal.proyectofinal.AbstractTest;
import com.proyectofinal.proyectofinal.service.SystemPromptService;
import com.proyectofinal.proyectofinal.types.IAResponseKeys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SystemPromptServiceTest extends AbstractTest {

    @Autowired
    SystemPromptService systemPromptService;

    @Test
    void getBasePrompt_containsAllIAResponseKeysWithDefinitions() {
        String prompt = systemPromptService.getLatest().getPrompt();

        for (IAResponseKeys key : IAResponseKeys.values()) {
            // Comprobamos que aparezca la clave exacta
            assertTrue(prompt.contains(key.name()), "Prompt should contain key: " + key.name());
            // Comprobamos que aparezca su definición
            assertTrue(prompt.contains(key.getDefinition()), "Prompt should contain definition for key: " + key.name());
        }
    }

    @Test
    void getBasePrompt_containsFormattingSection() {
        String prompt = systemPromptService.getLatest().getPrompt();

        // Verificamos que contenga la sección de formato
        assertTrue(prompt.contains("FORMATO DE RESPUESTA:"), "Prompt should contain formatting header");
    }
}