package com.proyectofinal.proyectofinal.service;

import com.proyectofinal.proyectofinal.dto.app_user.AppUserCreationDTO;
import com.proyectofinal.proyectofinal.model.AppUser;
import com.proyectofinal.proyectofinal.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserService extends AbstractService<AppUser, AppUserRepository> {
    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        super(appUserRepository, AppUser.class);
        repository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<AppUser> findActiveByEmail(String email) {
        return repository.findByEmailAndDeletedAtIsNull(email);
    }

    public AppUser create(AppUserCreationDTO creationDTO) {
        Optional<AppUser> opExistingUser = findActiveByEmail(creationDTO.getEmail());
        if (opExistingUser.isPresent()) {
            throw new IllegalArgumentException(String.format("AppUser with email %s already exists", creationDTO.getEmail()));
        }

        String encodedPassword = passwordEncoder.encode(creationDTO.getPassword());
        AppUser modelToCreate = AppUser.builder().email(creationDTO.getEmail()).password(encodedPassword).build();
        return repository.save(modelToCreate);
    }
}
