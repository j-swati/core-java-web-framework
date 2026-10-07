package com.framework.app.validation;

import com.framework.exception.ValidationException;

import java.util.Objects;

public final class UserValidator {

    private UserValidator() {
    }

    public static void validate(String name, String email) {
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(email, "Email cannot be null");

        if (name.trim().isEmpty()) {
            throw new ValidationException("Name cannot be blank");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new ValidationException("Invalid email format: " + email);
        }
    }
}


// Private constructor prevents object creation.
// Static validate() can be called directly using UserValidator.validate().
// Objects.requireNonNull() -- "This value must not be null; if it is, immediately throw an exception."
