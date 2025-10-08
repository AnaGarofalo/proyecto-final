package com.proyectofinal.proyectofinal.repository;

import com.proyectofinal.proyectofinal.model.ChatUser;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatUserRepository extends AbstractRepository<ChatUser>{
    Optional<ChatUser> findByPhoneNumberAndDeletedAtIsNull(String phoneNumber);

    List<ChatUser> findByDeletedAtIsNull();
}
