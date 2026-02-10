package com.socialapp.sfll.security.filter;

import com.socialapp.sfll.security.context.AuthContext;
import com.socialapp.sfll.ratelimit.RateLimiter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class RateLimitFilter implements Filter {

    private final RateLimiter limiter;

    @Override
    public void doFilter(
            ServletRequest req,
            ServletResponse res,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

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

        chain.doFilter(req, res);
    }

    private String resolveKey(HttpServletRequest request) {

        var auth = AuthContext.get();

        if (auth != null && auth.getAuth() != null) {
            return "USER:" + auth.getAuth().getUserId();
        }

        return "IP:" + request.getRemoteAddr();
    }
}