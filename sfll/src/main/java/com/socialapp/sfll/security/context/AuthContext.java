package com.socialapp.sfll.security.context;

import com.socialapp.sfll.security.auth.AuthResult;

/**
 * Authentication Context.
 * <p>
 * This class provides a thread-local storage for authentication results.
 * It allows the authentication status to be accessed and modified
 * during the processing of requests.
 */
public class AuthContext {

    private static final ThreadLocal<AuthResult> CONTEXT = new ThreadLocal<>();

    /**
     * Sets the AuthResult for the current thread.
     *
     * @param result the AuthResult object to be stored
     */
    public static void set(AuthResult result) {
        CONTEXT.set(result);
    }

    /**
     * Retrieves the AuthResult for the current thread.
     *
     * @return the AuthResult object associated with the current thread, or null if none exists
     */
    public static AuthResult get() {
        return CONTEXT.get();
    }

    /**
     * Clears the AuthResult for the current thread.
     */
    public static void clear() {
        CONTEXT.remove();
    }
}