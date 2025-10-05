package com.proyectofinal.proyectofinal.controller;

import com.proyectofinal.proyectofinal.dto.app_user.AppUserLoginDTO;
import com.proyectofinal.proyectofinal.dto.app_user.AppUserMinimalDTO;
import com.proyectofinal.proyectofinal.model.AppUser;
import com.proyectofinal.proyectofinal.service.AppUserService;
import com.proyectofinal.proyectofinal.utils.EntityMapper;
import com.proyectofinal.proyectofinal.security.JwtUtil;
import com.proyectofinal.proyectofinal.validations.UserValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app-user")
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AppUserLoginDTO appUserLoginDTO) {
        UserValidation.validateEmail(appUserLoginDTO.getEmail());
        UserValidation.validatePassword(appUserLoginDTO.getPassword());
        AppUser appUser = appUserService.getByEmail(appUserLoginDTO.getEmail());
        // Aquí deberías validar la contraseña también
        String token = jwtUtil.generateToken(appUser.getEmail());
        return ResponseEntity.ok(Map.of("token", token));
    }

    // Crear usuario
    @PostMapping
    public ResponseEntity<AppUserMinimalDTO> createUser(@RequestBody AppUserLoginDTO dto) {
        UserValidation.validateEmail(dto.getEmail());
        UserValidation.validatePassword(dto.getPassword());
        AppUser appUser = appUserService.create(dto);
        AppUserMinimalDTO responseDto = EntityMapper.map(appUser, AppUserMinimalDTO.class);
        return ResponseEntity.ok(responseDto);
    }

    // Obtener todos los usuarios activos
    @GetMapping
    public ResponseEntity<List<AppUserMinimalDTO>> getAllUsers() {
        List<AppUser> users = appUserService.findAllActive();
        List<AppUserMinimalDTO> dtos = users.stream()
                .map(user -> EntityMapper.map(user, AppUserMinimalDTO.class))
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // Obtener usuario por email
    @GetMapping("/{email}")
    public ResponseEntity<AppUserMinimalDTO> getUserByEmail(@PathVariable String email) {
        UserValidation.validateEmail(email);
        AppUser user = appUserService.getByEmail(email);
        AppUserMinimalDTO dto = EntityMapper.map(user, AppUserMinimalDTO.class);
        return ResponseEntity.ok(dto);
    }

    // Actualizar usuario (email y password)
@PutMapping("/{externalId}")
public ResponseEntity<AppUserMinimalDTO> updateUser(@PathVariable String externalId, @RequestBody AppUserLoginDTO dto) {
    UserValidation.validateEmail(dto.getEmail());
    UserValidation.validatePassword(dto.getPassword());
    AppUser updatedUser = appUserService.updateEmailAndPasswordByExternalId(externalId, dto.getEmail(), dto.getPassword());
    AppUserMinimalDTO responseDto = EntityMapper.map(updatedUser, AppUserMinimalDTO.class);
    return ResponseEntity.ok(responseDto);
}

    // Delete lógico por externalId
@DeleteMapping("/{externalId}")
public ResponseEntity<Void> deleteUser(@PathVariable String externalId) {
    appUserService.softDeleteByExternalId(externalId);
    return ResponseEntity.noContent().build();
}

}
