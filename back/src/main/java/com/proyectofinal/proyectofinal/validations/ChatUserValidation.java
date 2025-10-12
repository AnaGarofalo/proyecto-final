package com.proyectofinal.proyectofinal.validations;

public class ChatUserValidation {
    public static void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("^\\+549\\d{10}$")) {
            throw new IllegalArgumentException("Invalid phone number");
        }
    }
}
