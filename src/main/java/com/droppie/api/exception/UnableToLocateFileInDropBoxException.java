package com.droppie.api.exception;

public class UnableToLocateFileInDropBoxException extends Exception {
    public UnableToLocateFileInDropBoxException() {
        super("Requested file was not found on Dropbox. Please make sure you have provided correct file name.");
    }
}
