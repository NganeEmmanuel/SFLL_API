package com.socialapp.sfll.security.filter;

import com.socialapp.sfll.security.auth.Authenticator;
import com.socialapp.sfll.security.context.AuthContext;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class AuthFilter implements Filter {

    private final List<Authenticator> authenticators;

    @Override
    public void doFilter(
            ServletRequest req,
            ServletResponse res,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        for (Authenticator auth : authenticators) {

            if (!auth.supports(request)) {
                continue;
            }

            var result = auth.authenticate(request);

            if (result.isAuthenticated()) {

                try {
                    AuthContext.set(result);
                    chain.doFilter(req, res);
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