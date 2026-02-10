package com.socialapp.sfll.ratelimit;

public interface RateLimiter {
    boolean allowRequest(String key);
}