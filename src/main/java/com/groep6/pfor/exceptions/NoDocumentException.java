package com.groep6.pfor.exceptions;

/**
 * no document exception for importing json files.
 * @author Nils van der Velden
 */
public class NoDocumentException extends Exception {

    private final String exceptionMessage;

    public NoDocumentException(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public NoDocumentException() {
        exceptionMessage = "No such document";
    }

    @Override
    public String getMessage() {
        return exceptionMessage;
    }
}
