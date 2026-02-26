package com.socialapp.sfll.AuthService.security.interceptor;

import com.socialapp.sfll.AuthService.security.context.AuthContext;
import jakarta.servlet.http.*;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Role Interceptor.
 *
 * This interceptor checks if the authenticated user has the required role
 * to access a given resource. If the user does not have the required role,
 * a RuntimeException is thrown.
 */
public class RoleInterceptor implements HandlerInterceptor {

    private final String requiredRole;

    /**
     * Constructor for RoleInterceptor.
     *
     * @param role the role required for access to the resource.
     */
    public RoleInterceptor(String role) {
        this.requiredRole = role;
    }

    /**
     * Pre-handle method that checks if the authenticated user has the required role.
     *
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @param handler the chosen handler to execute
     * @return boolean indicating if the request should proceed
     * @throws RuntimeException if the user is not authorized
     */
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        var auth = AuthContext.get();
        if (auth == null || !auth.getAuth().getRoles().contains(requiredRole)) {
            throw new RuntimeException("Forbidden");
        }
        return true;
    }
}