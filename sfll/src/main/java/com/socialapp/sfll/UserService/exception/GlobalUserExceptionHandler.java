package com.socialapp.sfll.UserService.exception;

import com.socialapp.sfll.AuthService.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@SuppressWarnings("unused")
public class GlobalUserExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String userNotFoundException(UserNotFoundException ex, WebRequest request) {
        return ex.getMessage();
    }

    @ExceptionHandler(UserNotAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleUserNotAuthorizedException(UserNotAuthorizedException ex, WebRequest request) {
        return ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(
                        error.getField(),
                        error.getDefaultMessage()
                ));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public  String handleForbiddenException(ForbiddenException ex, WebRequest request) {
        return ex.getMessage();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRuntimeException(RuntimeException ex, WebRequest request) {
        return  ex.getMessage();
    }
}
