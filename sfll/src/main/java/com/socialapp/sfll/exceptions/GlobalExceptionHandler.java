package com.socialapp.sfll.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String hadleValidationException(ValidationException ex, WebRequest request) {
        return ex.getMessage();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String userNotFoundException(UserNotFoundException ex, WebRequest request) {
        return ex.getMessage();
    }

    @ExceptionHandler(ReadFileException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleReadFileException(ReadFileException ex, WebRequest request) {
        return ex.getMessage();
    }
}
