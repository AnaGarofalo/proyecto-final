package com.proyectofinal.proyectofinal.repository;

import com.proyectofinal.proyectofinal.model.AppUser;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import java.util.List;

@Repository
public interface AppUserRepository extends AbstractRepository<AppUser> {
    Optional<AppUser> findByEmailAndDeletedAtIsNull(String email);

    // Buscar todos los usuarios no eliminados (activos)
    List<AppUser> findByDeletedAtIsNull();


}
