package com.groep6.pfor.exceptions;

/**
 * empty field exception for hosting and joining a lobby
 * @author Nils van der Velden
 */
public class EmptyFieldException extends Exception {

    private final String exceptionMessage;

    public EmptyFieldException(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public EmptyFieldException() {
        exceptionMessage = "Field cannot be empty";
    }

    @Override
    public String getMessage() {
        return exceptionMessage;
    }
}
