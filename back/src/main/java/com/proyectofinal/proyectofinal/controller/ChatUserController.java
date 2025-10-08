package com.proyectofinal.proyectofinal.controller;

import com.proyectofinal.proyectofinal.dto.ChatUserDTO;
import com.proyectofinal.proyectofinal.dto.app_user.AppUserMinimalDTO;
import com.proyectofinal.proyectofinal.model.AppUser;
import com.proyectofinal.proyectofinal.model.ChatUser;
import com.proyectofinal.proyectofinal.service.ChatUserService;
import com.proyectofinal.proyectofinal.utils.EntityMapper;
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