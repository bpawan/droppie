package com.droppie.api.exception;

public class UnableToStoreTagsException extends Exception {
    public UnableToStoreTagsException() {
        super("Unable add tags to the requested file. Please make sure that you provided correct filepath.");
    }
}
