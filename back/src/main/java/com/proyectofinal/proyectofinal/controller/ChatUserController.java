package com.proyectofinal.proyectofinal.controller;

import com.proyectofinal.proyectofinal.dto.chat_user.ChatUserDTO;
import com.proyectofinal.proyectofinal.dto.chat_user.CreateChatUserDTO;
import com.proyectofinal.proyectofinal.model.ChatUser;
import com.proyectofinal.proyectofinal.service.ChatUserService;
import com.proyectofinal.proyectofinal.utils.EntityMapper;
import com.proyectofinal.proyectofinal.validations.ChatUserValidation;
import com.proyectofinal.proyectofinal.validations.UserValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat-user")
@RequiredArgsConstructor
public class ChatUserController {
    private final ChatUserService chatUserService;

    @GetMapping
    public ResponseEntity<List<ChatUserDTO>> getAllUsers() {
        List<ChatUser> users = chatUserService.getAllActive();
        List<ChatUserDTO> dtos = users.stream()
                .map(user -> EntityMapper.map(user, ChatUserDTO.class))
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<ChatUserDTO> create(@RequestBody CreateChatUserDTO createChatUserDTO) {
        UserValidation.validateEmail(createChatUserDTO.getEmail());
        ChatUserValidation.validatePhoneNumber(createChatUserDTO.getPhoneNumber());
        ChatUser chatUser = chatUserService.createByAdmin(createChatUserDTO.getEmail(), createChatUserDTO.getPhoneNumber());
        return ResponseEntity.ok(EntityMapper.map(chatUser, ChatUserDTO.class));
    }

    @PutMapping("/block/{externalId}")
    public ResponseEntity<ChatUserDTO> blockAppUser(@PathVariable("externalId") String externalId) {
        ChatUser chatUser = chatUserService.markAsBlocked(externalId);
        return ResponseEntity.ok(EntityMapper.map(chatUser, ChatUserDTO.class));
    }

    @PutMapping("/unblock/{externalId}")
    public ResponseEntity<ChatUserDTO> unblockAppUser(@PathVariable("externalId") String externalId) {
        ChatUser chatUser = chatUserService.unblock(externalId);
        return ResponseEntity.ok(EntityMapper.map(chatUser, ChatUserDTO.class));
    }

    @DeleteMapping("/{externalId}")
    public ResponseEntity<ChatUserDTO> deleteAppUser(@PathVariable("externalId") String externalId) {
        ChatUser chatUser = chatUserService.markAsDeleted(externalId);
        return ResponseEntity.ok(EntityMapper.map(chatUser, ChatUserDTO.class));
    }
}