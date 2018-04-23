package com.github.quiram.shopping.featureflags.exceptions;

import static java.lang.String.format;

public class FlagWithoutIdException extends Exception {
    public FlagWithoutIdException(String flagName) {
        super(format("Flag with name '%s' does not have a flagId", flagName));
    }
}
