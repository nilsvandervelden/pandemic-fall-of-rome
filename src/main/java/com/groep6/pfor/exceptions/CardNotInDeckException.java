package com.groep6.pfor.exceptions;

public class CardNotInDeckException extends Exception {
    private final String exceptionMessage;

    public CardNotInDeckException(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public CardNotInDeckException() {
        exceptionMessage = "Requested card was not found in this deck";
    }

    @Override
    public String getMessage() {
        return exceptionMessage;
    }
}

