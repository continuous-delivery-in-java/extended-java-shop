package com.github.quiram.featureflags.exceptions;

public class FlagNameAlreadyExistsException extends Exception {
    public FlagNameAlreadyExistsException(String message) {
        super(message);
    }
}
