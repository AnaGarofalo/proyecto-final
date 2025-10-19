package com.proyectofinal.proyectofinal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyectofinal.proyectofinal.model.ChatUser;
import com.proyectofinal.proyectofinal.model.Conversation;
import com.proyectofinal.proyectofinal.model.Ticket;
import com.proyectofinal.proyectofinal.repository.TicketRepository;

@Service
public class TicketService extends AbstractService<Ticket, TicketRepository> {
    public TicketService(TicketRepository repository) {
        super(repository, Ticket.class);
    }

    public void create(ChatUser chatUser, String content, Conversation conversation) {
        var ticket = Ticket.builder()
                .chatUser(chatUser)
                .conversation(conversation)
                .content(content)
                .build();
        repository.save(ticket);
    }
}
