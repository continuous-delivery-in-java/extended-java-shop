package com.github.quiram.shopping.featureflags.exceptions;

public class FlagNameAlreadyExistsException extends Exception {
    public FlagNameAlreadyExistsException(String message) {
        super(message);
    }
}
