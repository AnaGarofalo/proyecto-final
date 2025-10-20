package com.proyectofinal.proyectofinal.repository;

import com.proyectofinal.proyectofinal.model.SystemPrompt;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemPromptRepository extends AbstractRepository<SystemPrompt> {
    List<SystemPrompt> findAll();
}
