package com.droppie.api.advice;

import com.droppie.api.controller.TagsController;
import com.droppie.api.exception.UnableToStoreTagsException;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice(assignableTypes = TagsController.class)
public class UnableToStoreTagsAdvice {
    @ResponseBody
    @ExceptionHandler(UnableToStoreTagsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> fileNotFoundHandler(UnableToStoreTagsException exception) {
        val response = new HashMap<String, String>(1);

        response.put("message", exception.getMessage());

        return response;
    }
}
