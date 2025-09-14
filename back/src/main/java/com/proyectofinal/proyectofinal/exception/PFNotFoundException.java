package com.proyectofinal.proyectofinal.exception;

public class PFNotFoundException extends RuntimeException {
    public PFNotFoundException(String identifier, String identifierType, Class<?> objectClass) {
        super(String.format("%s with %s %s not found", objectClass.getSimpleName(), identifierType, identifier));
    }
}