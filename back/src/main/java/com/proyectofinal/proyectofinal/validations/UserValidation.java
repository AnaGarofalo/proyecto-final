package com.proyectofinal.proyectofinal.validations;

public class UserValidation {

    public static void validateEmail(String email) {
        if (email == null
            || email.contains(" ")
            || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("invalid email format");
        }
    }

    public static void validatePassword(String password) {
        if (password == null
            || password.length() < 6
            || password.contains(" ")
            || !password.matches(".*[A-Z].*")
            || !password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("valid password format");
        }
    }
}