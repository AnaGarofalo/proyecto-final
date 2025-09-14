package com.proyectofinal.proyectofinal.repository;

import com.proyectofinal.proyectofinal.model.AppUser;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends AbstractRepository<AppUser> {
    Optional<AppUser> findByEmailAndDeletedAtIsNull(String email);
}
