package com.groep6.pfor.exceptions;

/**
 * username already taken exception for
 * @author Nils van der Velden
 */
public class UsernameAlreadyUsed extends Exception {

    private final String exceptionMessage;

    public UsernameAlreadyUsed(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public UsernameAlreadyUsed() {
        exceptionMessage = "Username already used";
    }

    public String getMessage() {
        return exceptionMessage;
    }
}
