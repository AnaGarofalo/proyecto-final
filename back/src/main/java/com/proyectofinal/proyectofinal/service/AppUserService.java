package com.proyectofinal.proyectofinal.service;

import com.proyectofinal.proyectofinal.dto.app_user.AppUserLoginDTO;
import com.proyectofinal.proyectofinal.exception.PFNotFoundException;
import com.proyectofinal.proyectofinal.model.AppUser;
import com.proyectofinal.proyectofinal.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public AppUser create(AppUserLoginDTO creationDTO) {
        Optional<AppUser> opExistingUser = findActiveByEmail(creationDTO.getEmail());
        if (opExistingUser.isPresent()) {
            throw new IllegalArgumentException(String.format("AppUser with email %s already exists", creationDTO.getEmail()));
        }

        String encodedPassword = passwordEncoder.encode(creationDTO.getPassword());
        AppUser modelToCreate = AppUser.builder().email(creationDTO.getEmail()).password(encodedPassword).build();
        return repository.save(modelToCreate);
    }

    public AppUser updateEmailAndPassword(String currentEmail, String newEmail, String newPassword) {
    AppUser user = getByEmail(currentEmail);
    user.setEmail(newEmail);
    user.setPassword(passwordEncoder.encode(newPassword));
    return repository.save(user);
}


    // Obtener todos los usuarios activos (no eliminados)
    public List<AppUser> findAllActive() {
        return repository.findByDeletedAtIsNull();
    }

    // Actualizar password de usuario por email
    public AppUser updatePassword(String email, String newPassword) {
        AppUser user = getByEmail(email);
        user.setPassword(passwordEncoder.encode(newPassword));
        return repository.save(user);
    }

    // Delete lógico por email
    public void softDeleteByEmail(String email) {
        AppUser user = getByEmail(email);
        user.setDeletedAt(java.time.LocalDateTime.now());
        repository.save(user);
    }

    // Resetear password por email
    public AppUser resetPassword(String email, String newPassword) {
        return updatePassword(email, newPassword);
    }

}
