package com.socialapp.sfll.AuthService.exception;

public class UserNotAuthorizedException extends RuntimeException {
    public UserNotAuthorizedException(String message) {
        super(message);
    }
}
