package com.droppie.service.exception;

public class CannotCreateDownloadFolderException extends FileDownloadException {
    public CannotCreateDownloadFolderException() {
        super("Failed to create the file download folder.");
    }
}
