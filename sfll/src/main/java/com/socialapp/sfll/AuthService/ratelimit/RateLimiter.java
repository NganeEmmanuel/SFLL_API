package com.socialapp.sfll.AuthService.ratelimit;

public interface RateLimiter {
    boolean allowRequest(String key);
}