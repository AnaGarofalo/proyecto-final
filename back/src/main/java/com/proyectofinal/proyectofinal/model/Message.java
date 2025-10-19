package com.proyectofinal.proyectofinal.model;

import com.proyectofinal.proyectofinal.types.MessageOrigin;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Message extends AbstractModel {
    @ManyToOne(fetch = FetchType.LAZY)              // fetch preferible LAZY para evitar ciclos
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING) private MessageOrigin origin;
}
