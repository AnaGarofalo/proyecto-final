package com.proyectofinal.proyectofinal.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IAResponseKeys {
    USER_RESPONSE("La respuesta que le vas a dar al usuario"),
    TICKET_CONTENT("El contenido del ticket (si corresponde, sino devolvé un String vacío)");

    final String definition;
}
