package com.groep6.pfor.exceptions;

/**
 * IncorrectPasswordException for lobby login
 * @author Nils van der Velden
 */
public class IncorrectPasswordException extends Exception {

    private final String exceptionMessage;

    public IncorrectPasswordException(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public IncorrectPasswordException() {
        exceptionMessage = "Incorrect password";
    }

    @Override
    public String getMessage() {
        return exceptionMessage;
    }
}
