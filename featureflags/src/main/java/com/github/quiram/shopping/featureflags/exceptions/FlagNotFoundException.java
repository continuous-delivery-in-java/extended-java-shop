package com.github.quiram.shopping.featureflags.exceptions;

public class FlagNotFoundException extends Exception {

    public FlagNotFoundException() {
    }

    public FlagNotFoundException(String message) {
        super(message);
    }

    public FlagNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
