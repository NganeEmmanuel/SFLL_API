package com.socialapp.sfll.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Set;

@Slf4j
public class CorsFilter implements Filter {

    // Allowed origins (move to config later)
    private static final Set<String> ALLOWED_ORIGINS = Set.of(
            "http://localhost:3000",
            "http://127.0.0.1:3000"
    );

    @Override
    public void doFilter(
            ServletRequest req,
            ServletResponse res,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String origin = request.getHeader("Origin");

        if (origin != null && ALLOWED_ORIGINS.contains(origin)) {

            response.setHeader(
                    "Access-Control-Allow-Origin",
                    origin
            );

            response.setHeader(
                    "Vary",
                    "Origin"
            );
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

        // Handle preflight
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {

            log.debug("CORS preflight request handled");

            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(req, res);
    }
}