package com.proyectofinal.proyectofinal.service;

import com.proyectofinal.proyectofinal.exception.PFNotFoundException;
import com.proyectofinal.proyectofinal.model.AbstractModel;
import com.proyectofinal.proyectofinal.repository.AbstractRepository;

import java.util.List;
import java.util.Optional;

public class AbstractService<M extends AbstractModel, R extends AbstractRepository<M>> {
    protected final R repository;
    private final Class<M> modelClass;

    public AbstractService(R repository, Class<M> modelClass) {
        this.repository = repository;
        this.modelClass = modelClass;
    }

    public Optional<M> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<M> findByExternalId(String id) {
        return repository.findByExternalId(id);
    }

    public M getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new PFNotFoundException(id.toString(), "id", modelClass));
    }

    public M getByExternalId(String externalId) {
        return repository.findByExternalId(externalId)
                .orElseThrow(() -> new PFNotFoundException(externalId, "externalId", modelClass));
    }

    public List<M> getAllActive() {
        return repository.findByDeletedAtIsNull();
    }

    /*
        Sirve tanto para crear como para actualizar
     */
    public M save(M model) {
        return repository.save(model);
    }
}
