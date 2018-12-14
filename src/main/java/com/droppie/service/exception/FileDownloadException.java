package com.droppie.service.exception;


public class FileDownloadException extends Exception {
    FileDownloadException(String message) {
        super(message);
    }

    FileDownloadException(String message, Throwable previous) {
        super(message, previous);
    }
}
