package com.proyectofinal.proyectofinal.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PremadeResponse {
    NO_USER("Para comunicarte con el asistente virtual, por favor ingresá tu email empresarial"),
    CREATED_USER("Muchas gracias! Ya estás autorizado a comunicarte con el asistente virtual"),
    BLOCKED_USER("No estás autorizado para comunicarte con el asistente virtual"),
    ERROR("Ups! Hubo un error al intentar procesar tu solicitud, por favor intentá de nuevo más tarde");

    private final String message;
}
