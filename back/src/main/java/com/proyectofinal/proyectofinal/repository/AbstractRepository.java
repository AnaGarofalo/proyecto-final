package com.proyectofinal.proyectofinal.repository;

import com.proyectofinal.proyectofinal.model.AbstractModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface AbstractRepository<T extends AbstractModel> extends JpaRepository<T, Long> {
    Optional<T> findByExternalId(String externalId);

    List<T> findByDeletedAtIsNull();
}
