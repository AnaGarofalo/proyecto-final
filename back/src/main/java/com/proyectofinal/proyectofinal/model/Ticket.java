package com.proyectofinal.proyectofinal.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Ticket extends AbstractModel {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_user_id")
    private ChatUser chatUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @Column(nullable = false)
    private String content;
}