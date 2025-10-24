package com.proyectofinal.proyectofinal.repository;

import org.springframework.stereotype.Repository;

import com.proyectofinal.proyectofinal.model.Ticket;

import java.util.List;

@Repository 
public interface TicketRepository extends AbstractRepository<Ticket> {
    List<Ticket> findAllByOrderByCreatedAtDesc();
}