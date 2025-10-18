package com.proyectofinal.proyectofinal.service;

import com.proyectofinal.proyectofinal.dto.app_user.AppUserLoginDTO;
import com.proyectofinal.proyectofinal.exception.PFNotFoundException;
import com.proyectofinal.proyectofinal.model.AppUser;
import com.proyectofinal.proyectofinal.repository.AppUserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import java.util.Optional;

@Service
public class AppUserService extends AbstractService<AppUser, AppUserRepository> {
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        super(appUserRepository, AppUser.class);
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<AppUser> findActiveByEmail(String email) {
        return repository.findByEmailAndDeletedAtIsNull(email);
    }

    public AppUser getByEmail(String email) {
        return findActiveByEmail(email)
                .orElseThrow(() -> new PFNotFoundException(email, "email", AppUser.class));
    }

    public AppUser getFromToken() {
        // Obtener el email del usuario autenticado desde el contexto de seguridad
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Buscar el usuario activo por email
        return findActiveByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    // Método para validar login (email + password)
    public AppUser validateLogin(String email, String password) {
        AppUser user = getByEmail(email);
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("invalid credentials");
        }
        
        // Verificar si el usuario está bloqueado (A revisar, aun no definimos como se bloquea)
        if (user.getBlockedAt() != null) {
            throw new IllegalArgumentException("User is blocked");
        }
        
        return user;
    }

    public AppUser create(AppUserLoginDTO creationDTO) {
        Optional<AppUser> opExistingUser = findActiveByEmail(creationDTO.getEmail());
        if (opExistingUser.isPresent()) {
            throw new IllegalArgumentException(
                    String.format("AppUser with email %s already exists", creationDTO.getEmail()));
        }

        String encodedPassword = passwordEncoder.encode(creationDTO.getPassword());
        AppUser modelToCreate = AppUser.builder().email(creationDTO.getEmail()).password(encodedPassword).build();
        return repository.save(modelToCreate);
    }

    public AppUser updateUserByExternalId(String externalId, String newEmail, String newPassword) {
        AppUser user = repository.findByExternalIdAndDeletedAtIsNull(externalId)
                .orElseThrow(() -> new PFNotFoundException(externalId, "externalId", AppUser.class));
        
        user.setEmail(newEmail);
        
        if (newPassword != null && !newPassword.isEmpty()) {
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);
        }
        
        return repository.save(user);
    }

    // Obtener todos los usuarios activos (no eliminados)
    public List<AppUser> findAllActive() {
        return repository.findByDeletedAtIsNull();
    }

    public void softDeleteByExternalId(String externalId) {
        AppUser user = repository.findByExternalIdAndDeletedAtIsNull(externalId)
                .orElseThrow(() -> new PFNotFoundException(externalId, "externalId", AppUser.class));
        user.setDeletedAt(LocalDateTime.now());
        repository.save(user);
    }

}
