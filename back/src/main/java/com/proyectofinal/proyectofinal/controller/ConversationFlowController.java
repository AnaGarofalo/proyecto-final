package com.proyectofinal.proyectofinal.controller;

import com.proyectofinal.proyectofinal.dto.MessageDTO;
import com.proyectofinal.proyectofinal.dto.SystemPromptDTO;
import com.proyectofinal.proyectofinal.model.Conversation;
import com.proyectofinal.proyectofinal.service.ConversationFlowService;
import com.proyectofinal.proyectofinal.service.ConversationService;
import com.proyectofinal.proyectofinal.utils.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conversation-flow")
@RequiredArgsConstructor
public class ConversationFlowController {
    private final ConversationFlowService conversationFlowService;
    private final ConversationService conversationService;

    @PostMapping("/chat")
    public ResponseEntity<MessageDTO> chat(@RequestBody MessageDTO message) {
        return ResponseEntity.ok(conversationFlowService.getResponseForAppUserMessage(message.getContent()));
    }

    @GetMapping("/conversation")
    public ResponseEntity<List<MessageDTO>> getActiveConversation() {
        Conversation conversation = conversationService.getOrCreateActiveConversationByAppUser();
        List<MessageDTO> messageDTOS = conversation.getMessages().stream().map((element) -> EntityMapper.map(element, MessageDTO.class)).toList();
        return ResponseEntity.ok(messageDTOS);
    }
}
