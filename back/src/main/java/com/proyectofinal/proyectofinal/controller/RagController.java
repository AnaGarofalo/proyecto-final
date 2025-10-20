package com.proyectofinal.proyectofinal.controller;

import com.proyectofinal.proyectofinal.dto.IAResponseDTO;
import com.proyectofinal.proyectofinal.dto.app_user.ChatRequestDTO;
import com.proyectofinal.proyectofinal.dto.app_user.ChatResponseDTO;
import com.proyectofinal.proyectofinal.service.RagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rag")
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    // Preguntas al modelo, es solo para pruebas y luego será removido
    @PostMapping("/chat")
    public ResponseEntity<ChatResponseDTO> chat(@RequestBody ChatRequestDTO req) {
        IAResponseDTO answer = ragService.ask(req.getQuestion());
        List<String> sources = ragService.topSources(req.getQuestion());

        ChatResponseDTO resp = ChatResponseDTO.builder()
                .answer(answer.getUserResponse())
                .sources(sources)
                .build();

        return ResponseEntity.ok(resp);
    }

}