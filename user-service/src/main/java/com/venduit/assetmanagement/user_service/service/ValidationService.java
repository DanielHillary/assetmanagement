package com.venduit.assetmanagement.user_service.service;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class ValidationService {

    // Regular expressions for validation
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PHONE_REGEX = "^\\+?[1-9]\\d{1,14}$"; // E.164 format
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$";

    /**
     * Validates an email address.
     *
     * @param email The email to validate.
     * @return true if the email is valid, false otherwise.
     */
    public boolean validateEmail(String email) {
        return Pattern.compile(EMAIL_REGEX).matcher(email).matches();
    }

    /**
     * Validates a phone number.
     *
     * @param phoneNumber The phone number to validate.
     * @return true if the phone number is valid, false otherwise.
     */
    public boolean validatePhoneNumber(String phoneNumber) {
        return Pattern.compile(PHONE_REGEX).matcher(phoneNumber).matches();
    }

    /**
     * Validates a password.
     * Criteria:
     * - At least 8 characters long
     * - Contains at least one digit
     * - Contains at least one lowercase letter
     * - Contains at least one uppercase letter
     * - Contains at least one special character
     *
     * @param password The password to validate.
     * @return true if the password is valid, false otherwise.
     */
    public boolean validatePassword(String password) {
        return Pattern.compile(PASSWORD_REGEX).matcher(password).matches();
    }
}
