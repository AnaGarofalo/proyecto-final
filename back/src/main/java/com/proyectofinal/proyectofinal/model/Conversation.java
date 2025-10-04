package com.proyectofinal.proyectofinal.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Conversation extends AbstractModel {
    private static final Long TIMEOUT_MINUTES = 60L;

    @ManyToOne
    private ChatUser chatUser;
    private LocalDateTime finalizedAt;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default List<Message> messages = new ArrayList<>();

    public LocalDateTime getLastMessageTime() {
        if (messages.isEmpty()) {
            return this.getCreatedAt();
        }
        return messages.getLast().getCreatedAt();
    }

    public boolean isRecentConversation() {
        LocalDateTime lastMessageTime = getLastMessageTime();
        return lastMessageTime.isAfter(LocalDateTime.now().minusMinutes(TIMEOUT_MINUTES));
    }
}
