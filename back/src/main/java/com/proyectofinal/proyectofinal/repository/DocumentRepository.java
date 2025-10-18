package com.proyectofinal.proyectofinal.repository;

import com.proyectofinal.proyectofinal.model.Document;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository 
public interface DocumentRepository extends AbstractRepository<Document> {
    List<Document> findAllByDeletedAtIsNull();
}