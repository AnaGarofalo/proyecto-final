package com.proyectofinal.proyectofinal.service;

import com.proyectofinal.proyectofinal.dto.app_user.AppUserLoginDTO;
import com.proyectofinal.proyectofinal.exception.PFNotFoundException;
import com.proyectofinal.proyectofinal.model.AppUser;
import com.proyectofinal.proyectofinal.repository.AppUserRepository;
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

    public AppUser create(AppUserLoginDTO creationDTO) {
        Optional<AppUser> opExistingUser = findActiveByEmail(creationDTO.getEmail());
        if (opExistingUser.isPresent()) {
            throw new IllegalArgumentException(String.format("AppUser with email %s already exists", creationDTO.getEmail()));
        }

        String encodedPassword = passwordEncoder.encode(creationDTO.getPassword());
        AppUser modelToCreate = AppUser.builder().email(creationDTO.getEmail()).password(encodedPassword).build();
        return repository.save(modelToCreate);
    }

    public AppUser updateEmailAndPasswordByExternalId(String externalId, String newEmail, String newPassword) {
    AppUser user = repository.findByExternalIdAndDeletedAtIsNull(externalId)
        .orElseThrow(() -> new PFNotFoundException(externalId, "externalId", AppUser.class));
    user.setEmail(newEmail);
    user.setPassword(passwordEncoder.encode(newPassword));
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
