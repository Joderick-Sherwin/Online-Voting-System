package com.onlinevotingsystem.util;

public class InputValidator {
    // Check if a string is empty or null
    public static boolean isNullOrEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }

    // Check if an email is valid
    public static boolean isEmailValid(String email) {
        // You can implement a more robust email validation
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    // Check if a date of birth is valid (you can customize the format)
    public static boolean isDateOfBirthValid(String dateOfBirth) {
        // You can implement a more precise date validation
        return dateOfBirth != null && dateOfBirth.matches("^\\d{4}-\\d{2}-\\d{2}$");
    }
}
