package com.socialapp.sfll.security.filter;

import com.socialapp.sfll.security.auth.Authenticator;
import com.socialapp.sfll.security.context.AuthContext;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("NullableProblems")
@RequiredArgsConstructor
@Component
public class AuthFilter extends OncePerRequestFilter {

    private final List<Authenticator> authenticators;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        for (Authenticator auth : authenticators) {

            // strategy pattern: each authenticator checks if it supports the request
            if (!auth.supports(request)) {
                continue;
            }

            var result = auth.authenticate(request);

            if (result.isAuthenticated()) {

                try {
                    AuthContext.set(result);
                    var authU = AuthContext.get();
                    filterChain.doFilter(request, response);
                } finally {
                    AuthContext.clear();
                }

                return;
            }
        }

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.getWriter().write("""
            {
              "error": "UNAUTHORIZED",
              "message": "Authentication required"
            }
        """);
    }
}