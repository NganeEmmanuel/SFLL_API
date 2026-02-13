package com.socialapp.sfll.security.filter;

import com.socialapp.sfll.security.context.AuthContext;
import com.socialapp.sfll.ratelimit.RateLimiter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@SuppressWarnings("NullableProblems")
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimiter limiter;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String key = resolveKey(request);

        if (!limiter.allowRequest(key)) {

            response.setContentType("application/json");
            response.setStatus(429);

            response.getWriter().write("""
            {
              "error":"RATE_LIMITED",
              "message":"Too many requests"
            }
            """);

            return;
        }

        filterChain.doFilter(request, response);
    }

    private String resolveKey(HttpServletRequest request) {

        var auth = AuthContext.get();

        if (auth != null && auth.getAuth() != null) {
            return "USER:" + auth.getAuth().getUserId();
        }

        return "IP:" + request.getRemoteAddr();
    }
}