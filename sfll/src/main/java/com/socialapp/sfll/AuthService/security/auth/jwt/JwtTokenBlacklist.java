package com.socialapp.sfll.AuthService.security.auth.jwt;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenBlacklist {
    private Map<String, Long> jwtBlacklist = new HashMap<>(); // Token with expiration time

    public void addTokenToBlacklist(String token, Date expirationTime) {
        jwtBlacklist.put(token, expirationTime.getTime() + 60000); // 1 minute after expiration
    }

    public boolean isTokenBlacklisted(String token) {
        Long removalTime = jwtBlacklist.get(token);
        if (removalTime != null) {
            // Check if the token should be removed from the blacklist. For Redis this will be done automatically
            if (System.currentTimeMillis() > removalTime) {
                jwtBlacklist.remove(token); // Remove it if expired
                return true; // Token is no longer blacklisted, but we consider it blacklisted for this check saving the overhead of validating the token again
            }
            return true; // Token is blacklisted
        }
        return false; // Token not found
    }
}
