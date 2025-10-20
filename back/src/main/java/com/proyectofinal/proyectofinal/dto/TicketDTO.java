package com.proyectofinal.proyectofinal.dto;

import com.proyectofinal.proyectofinal.model.Ticket;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDTO {
    private String externalId;
    private String uploadedBy;
    private String createdAt;
    private String content;

    public TicketDTO(Ticket ticket) {
        this.externalId = ticket.getExternalId();
        this.createdAt = ticket.getCreatedAt() != null ? ticket.getCreatedAt().toString() : null;
        this.uploadedBy = ticket.getChatUser() != null ? ticket.getChatUser().getEmail() : null;
        this.content = ticket.getContent();
    }
}
