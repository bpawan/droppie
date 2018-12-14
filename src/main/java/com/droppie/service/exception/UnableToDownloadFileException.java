package com.droppie.service.exception;

public class UnableToDownloadFileException extends FileDownloadException {
    public UnableToDownloadFileException(String filename, Throwable previous) {
        super("");
    }
}
