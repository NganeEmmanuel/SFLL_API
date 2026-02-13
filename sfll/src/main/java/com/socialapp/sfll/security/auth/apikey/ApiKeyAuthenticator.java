package com.socialapp.sfll.security.auth.apikey;

import com.socialapp.sfll.security.auth.AuthResult;
import com.socialapp.sfll.security.auth.AuthUser;
import com.socialapp.sfll.security.auth.Authenticator;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class ApiKeyAuthenticator implements Authenticator {

    // Demo storage (replace with DB/Redis later)
    private static final Map<String, AuthUser> API_KEYS = Map.of(
            "abc123", new AuthUser(10L, "EmLmt", Set.of("SERVICE")),
            "xyz789", new AuthUser(20L, "blmt", Set.of("PARTNER"))
    );

    @Override
    public boolean supports(HttpServletRequest request) {

        return request.getHeader("X-API-KEY") != null;
    }

    @Override
    public AuthResult authenticate(HttpServletRequest request) {

        String apiKey = request.getHeader("X-API-KEY");

        if (apiKey == null || apiKey.isBlank()) {
            return new AuthResult(false, null);
        }

        AuthUser user = API_KEYS.get(apiKey);

        if (user == null) {
            return new AuthResult(false, null);
        }

        return new AuthResult(true, user);
    }
}