package com.proyectofinal.proyectofinal.controller;

import com.proyectofinal.proyectofinal.dto.app_user.AppUserLoginDTO;
import com.proyectofinal.proyectofinal.dto.app_user.AppUserMinimalDTO;
import com.proyectofinal.proyectofinal.mapper.AppUser.AppUserMinimalDTOMapper;
import com.proyectofinal.proyectofinal.model.AppUser;
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
    public ResponseEntity<AppUserMinimalDTO> login(@RequestBody AppUserLoginDTO appUserLoginDTO) {
        AppUser appUser = appUserService.getByEmail(appUserLoginDTO.getEmail());
        AppUserMinimalDTO dto = AppUserMinimalDTOMapper.fromEntity(appUser);
        return ResponseEntity.ok(dto);
    }
}
