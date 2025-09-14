package com.proyectofinal.proyectofinal.controller;

import com.proyectofinal.proyectofinal.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app-user")
//@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;

    @PostMapping("/login")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("success");
    }
}
