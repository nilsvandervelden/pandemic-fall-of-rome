package com.groep6.pfor.exceptions;

public class PlayerHasLeftTheGameException extends Exception{
    private final String exceptionMessage;

    public PlayerHasLeftTheGameException(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public PlayerHasLeftTheGameException() {
        exceptionMessage = "Can not give turn to another player because the player who had the turn left the game. Please restart the game";
    }

    @Override
    public String getMessage() {
        return exceptionMessage;
    }
}
