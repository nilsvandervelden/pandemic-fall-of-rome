package com.groep6.pfor.exceptions;

/**
 * IncorrectPasswordException for lobby login
 * @author Nils van der Velden
 */
public class NoBarbariansInCityException extends Exception {

    private final String exceptionMessage;

    public NoBarbariansInCityException(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public NoBarbariansInCityException() {
        exceptionMessage = "There are no barbarians in the current city";
    }

    @Override
    public String getMessage() {
        return exceptionMessage;
    }
}