package com.proyectofinal.proyectofinal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyectofinal.proyectofinal.dto.email.EmailDTO;
import com.proyectofinal.proyectofinal.service.EmailSenderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
public class EmailSenderController {
    @Autowired
    private final EmailSenderService emailSenderService;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody EmailDTO dto) {
        emailSenderService.sendEmail(dto.getTo(), dto.getSubject(), dto.getContent());
        return ResponseEntity.ok("Email enviado correctamente!");
    }
}
