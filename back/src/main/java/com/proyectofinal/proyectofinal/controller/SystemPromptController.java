package com.proyectofinal.proyectofinal.controller;

import com.proyectofinal.proyectofinal.dto.SystemPromptDTO;
import com.proyectofinal.proyectofinal.service.SystemPromptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system-prompt")
@RequiredArgsConstructor
public class SystemPromptController {
    private final SystemPromptService systemPromptService;

    @GetMapping
    public ResponseEntity<SystemPromptDTO> getLatest() {
    SystemPromptDTO dto = systemPromptService.getLatest();
        if (dto == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(dto);
    }

    @PatchMapping
    public ResponseEntity<SystemPromptDTO> patch(@RequestBody SystemPromptDTO request) {
        SystemPromptDTO saved = systemPromptService.update(request.getPrompt(), request.getTicketEmail());
        return ResponseEntity.ok(saved);
    }
}
