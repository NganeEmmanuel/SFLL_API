package com.socialapp.sfll.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Slf4j
@Component
public class CorsFilter extends OncePerRequestFilter {

    private static final Set<String> ALLOWED_ORIGINS = Set.of(
            "http://localhost:3000",
            "http://127.0.0.1:3000"
    );

    @Override
    @SuppressWarnings("NullableProblems")
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String origin = request.getHeader("Origin");

        if (origin != null) {

            // Check if the origin is in the allowed list
            if (!ALLOWED_ORIGINS.contains(origin)) {

                log.warn("Blocked CORS origin: {}", origin); // log for demo

                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            response.setHeader(
                    "Access-Control-Allow-Origin",
                    origin
            );

            // A -> localhost:3000 -> : localhost:3000 -> A
            // B -> localhost:3001 -> : localhost:3001 -> B
            // The response.setHeader("Vary", "Origin"); instruction tells browser caches and intermediate CDNs (Content Delivery Networks)
            // that the response for a given URL can differ based on the Origin header sent in the request.
            response.setHeader("Vary", "Origin");
        }

        response.setHeader(
                "Access-Control-Allow-Methods",
                "GET,POST,PUT,DELETE,OPTIONS"
        );

        response.setHeader(
                "Access-Control-Allow-Headers",
                "Authorization,Content-Type,X-API-KEY"
        );

        response.setHeader(
                "Access-Control-Allow-Credentials",
                "true"
        );

        response.setHeader(
                "Access-Control-Max-Age",
                "3600"
        );

        // Handle preflight requests without passing them down the filter chain
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {

            log.debug("Handled CORS preflight"); // log for demo

            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        filterChain.doFilter(request, response);
    }
}