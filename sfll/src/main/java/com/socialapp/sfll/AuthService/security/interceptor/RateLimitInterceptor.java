package com.socialapp.sfll.AuthService.security.interceptor;


import com.socialapp.sfll.AuthService.security.context.AuthContext;
import com.socialapp.sfll.AuthService.exception.RateLimitException;
import com.socialapp.sfll.AuthService.ratelimit.RateLimiter;
import jakarta.servlet.http.*;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

public class RateLimitInterceptor implements HandlerInterceptor {

    private final RateLimiter limiter;
    private static final Logger log = LoggerFactory.getLogger(RateLimitInterceptor.class);

    public RateLimitInterceptor(RateLimiter limiter) {
        this.limiter = limiter;
    }

    @Override
    public boolean preHandle(
            @NonNull
            HttpServletRequest request,
            @NonNull
            HttpServletResponse response,
            @NonNull
            Object handler
    ) {

        log.info(">>> RateLimitInterceptor hit");
        String key = resolveKey(request);

        if (!limiter.allowRequest(key)) {

            response.setStatus(429);
            throw new RateLimitException("Too many requests");
        }

        return true;
    }

    private String resolveKey(HttpServletRequest request) {

        var auth = AuthContext.get();

        if (auth != null) {
            return "USER:" + auth.getAuth().getUserId();
        }

        return "IP:" + request.getRemoteAddr();
    }
}