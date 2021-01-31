package com.groep6.pfor.exceptions;

public class CouldNotFindLocalPlayerException extends Exception{
    private final String exceptionMessage;

    public CouldNotFindLocalPlayerException(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public CouldNotFindLocalPlayerException() {
        exceptionMessage = "Cant find a local player, please restart the game";
    }

    @Override
    public String getMessage() {
        return exceptionMessage;
    }
}
