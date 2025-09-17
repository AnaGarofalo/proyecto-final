package com.proyectofinal.proyectofinal.controller;

import com.proyectofinal.proyectofinal.dto.app_user.AppUserLoginDTO;
import com.proyectofinal.proyectofinal.dto.app_user.AppUserMinimalDTO;
import com.proyectofinal.proyectofinal.model.AppUser;
import com.proyectofinal.proyectofinal.service.AppUserService;
import com.proyectofinal.proyectofinal.utils.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app-user")
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;

    @PostMapping("/login")
    public ResponseEntity<AppUserMinimalDTO> login(@RequestBody AppUserLoginDTO appUserLoginDTO) {
        AppUser appUser = appUserService.getByEmail(appUserLoginDTO.getEmail());
        AppUserMinimalDTO dto = EntityMapper.map(appUser, AppUserMinimalDTO.class);
        return ResponseEntity.ok(dto);
    }
}
