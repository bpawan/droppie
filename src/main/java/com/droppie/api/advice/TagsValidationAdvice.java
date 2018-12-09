package com.droppie.api.advice;

import com.droppie.api.controller.TagsController;
import com.droppie.api.model.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(assignableTypes = TagsController.class)
public class TagsValidationAdvice {

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationError validationErrorHandler() {
        return ValidationError
                .builder()
                .errorMessage("request data validation failed.")
                .build();
    }
}
