package com.proyectofinal.proyectofinal.controller;

import com.proyectofinal.proyectofinal.dto.SystemPromptDTO;
import com.proyectofinal.proyectofinal.model.SystemPrompt;
import com.proyectofinal.proyectofinal.service.SystemPromptService;
import com.proyectofinal.proyectofinal.utils.EntityMapper;
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
        SystemPrompt systemPrompt = systemPromptService.getLatest();
        SystemPromptDTO dto = EntityMapper.map(systemPrompt, SystemPromptDTO.class);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping
    public ResponseEntity<SystemPromptDTO> patch(@RequestBody SystemPromptDTO request) {
        SystemPrompt saved = systemPromptService.update(request.getPrompt(), request.getTicketEmail());
        SystemPromptDTO dto = EntityMapper.map(saved, SystemPromptDTO.class);
        return ResponseEntity.ok(dto);
    }
}
