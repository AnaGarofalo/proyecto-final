package com.proyectofinal.proyectofinal.controller;

import com.proyectofinal.proyectofinal.dto.TicketDTO;
import com.proyectofinal.proyectofinal.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        List<TicketDTO> ticketsDTOs = service.getAllTickets()
                .stream()
                .map(TicketDTO::new)
                .toList();
        return ResponseEntity.ok(ticketsDTOs);
    }
}
