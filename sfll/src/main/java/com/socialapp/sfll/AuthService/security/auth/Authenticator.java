package com.socialapp.sfll.AuthService.security.auth;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Authenticator Interface.
 *
 * This interface defines the contract for authentication mechanisms.
 * Implementations should specify whether they support a given
 * HTTP request and provide a method to authenticate the request.
 */
public interface Authenticator {

    /**
     * Checks if the authenticator supports the given HTTP request.
     *
     * @param request the current HTTP request
     * @return true if the authenticator can handle the request, false otherwise
     */
    boolean supports(HttpServletRequest request);

    /**
     * Authenticates the given HTTP request.
     *
     * @param request the current HTTP request
     * @return an AuthResult object containing the authentication result
     */
    AuthResult authenticate(HttpServletRequest request);
}