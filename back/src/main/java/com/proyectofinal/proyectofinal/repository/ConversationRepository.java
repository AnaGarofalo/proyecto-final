package com.proyectofinal.proyectofinal.repository;

import com.proyectofinal.proyectofinal.model.Conversation;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends AbstractRepository<Conversation>{
    Optional<Conversation> findByChatUserIdAndFinalizedAtIsNull(Long userId);
}