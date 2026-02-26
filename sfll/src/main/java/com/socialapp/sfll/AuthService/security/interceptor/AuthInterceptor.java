package com.socialapp.sfll.AuthService.security.interceptor;

import com.socialapp.sfll.AuthService.security.context.AuthContext;
import com.socialapp.sfll.AuthService.exception.AuthException;
import com.socialapp.sfll.AuthService.security.auth.AuthResult;
import com.socialapp.sfll.AuthService.security.auth.Authenticator;
import jakarta.servlet.http.*;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

/**
 * Authentication Interceptor.
 *
 * This interceptor checks if incoming requests are authenticated by using
 * a list of Authenticator instances. If authentication fails, an
 * AuthException is thrown. It also manages the AuthContext by clearing
 * the authentication details after the request is completed.
 */
public class AuthInterceptor implements HandlerInterceptor {

    private final List<Authenticator> authenticators;

    /**
     * Constructor for AuthInterceptor.
     *
     * @param authenticators List of Authenticator implementations to use for authentication.
     */
    public AuthInterceptor(List<Authenticator> authenticators) {
        this.authenticators = authenticators;
    }

    /**
     * Pre-handle method that checks authentication for the incoming request.
     *
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @param handler the chosen handler to execute
     * @return boolean indicating if the request should proceed
     * @throws AuthException if authentication fails or no method is found
     */
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        for (Authenticator auth : authenticators) {
            if (auth.supports(request)) {
                AuthResult result = auth.authenticate(request);
                if (!result.isAuthenticated()) {
                    throw new AuthException("Not authorized");
                }
                AuthContext.set(result);
                return true;
            }
        }
        throw new AuthException("No authentication method found");
    }

    /**
     * Method called after the request has completed.
     * Clears the authentication context.
     *
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @param handler the handler that was executed
     * @param ex any exception thrown during the execution
     */
    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ) {
        AuthContext.clear();
    }
}