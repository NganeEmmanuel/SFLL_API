package com.socialapp.sfll.security.interceptor;

import jakarta.servlet.http.*;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * CORS (Cross-Origin Resource Sharing) Interceptor.
 * <p>
 * This interceptor is responsible for handling Cross-Origin
 * Resource Sharing by modifying the HTTP response headers.
 * It allows web applications running at different origins to
 * access resources on the server.
 */
public class CorsInterceptor implements HandlerInterceptor {

    private static final Logger log =
            LoggerFactory.getLogger(CorsInterceptor.class);

    /**
     * Pre-handle method that is executed before the actual handler
     * is executed. This method sets the necessary CORS headers
     * and handles preflight requests.
     *
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @param handler the chosen handler to execute
     * @return boolean value indicating whether the request should proceed
     * or be rejected
     */
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            @NonNull
            Object handler
    ) {
        log.info(">>> CorsInterceptor hit");
        // Set CORS headers to allow requests from specific origin
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");

        // Specify allowed HTTP methods
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");

        // Specify allowed headers in requests
        response.setHeader("Access-Control-Allow-Headers", "Authorization,Content-Type");

        // Indicate whether credentials should be included with the requests
        response.setHeader("Access-Control-Allow-Credentials", "true");

        // Specify how long the results of a preflight request can be cached
        response.setHeader("Access-Control-Max-Age", "3600");

        // Handle preflight requests (OPTIONS method)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            // Respond with OK status for preflight request
            response.setStatus(HttpServletResponse.SC_OK);
            return false; // Prevent further handling of the request
        }

        return true; // Proceed with the request handling
    }
}
