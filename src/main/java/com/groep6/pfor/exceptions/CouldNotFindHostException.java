package com.groep6.pfor.exceptions;

public class CouldNotFindHostException extends Exception {
    private final String exceptionMessage;

    public CouldNotFindHostException(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public CouldNotFindHostException() {
        exceptionMessage = "Could not find the host, please restart the game";
    }

    @Override
    public String getMessage() {
        return exceptionMessage;
    }
}
