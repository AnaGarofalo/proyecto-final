package com.proyectofinal.proyectofinal.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.stereotype.Service;

@Service
public class OpenAiService {

    private final ChatLanguageModel chatModel;

    // Se inyecta el modelo de chat (GPT-4o-mini)
    public OpenAiService(ChatLanguageModel chatModel) {
        this.chatModel = chatModel;
    }

    // Este prompt lo podemos editar como queramos nosotros
    public String generateAnswer(String question, String context) {
        String prompt = "Responde EXCLUSIVAMENTE usando el siguiente contexto. " +
                "Ignora cualquier conocimiento previo y no corrijas lo que está en el documento. " +
                "Si no está en el contexto, responde 'No lo sé'.\n\n" +
                "Contexto:\n" + context + "\n\n" +
                "Pregunta: " + question + "\nRespuesta:";

        // Aca se hace la llamada al modelo
        return chatModel.generate(prompt);
    }
}