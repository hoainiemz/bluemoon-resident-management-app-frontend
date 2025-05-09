package org.example.hellofx.model;

import org.example.hellofx.model.enums.ValidationState;

import java.util.Objects;

public record Validation(ValidationState state, String message) {
    public Validation {
        Objects.requireNonNull(state);
        Objects.requireNonNull(message);
    }
}
