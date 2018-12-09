package com.droppie.service.exception;

public class UnsupportedSearchOperationException extends Exception {
    public UnsupportedSearchOperationException() {
        super("Unsupported operation. Allowed operations are union operation ['AND', 'and', '&'] , intersection operation ['OR', 'or', '|']");
    }
}
