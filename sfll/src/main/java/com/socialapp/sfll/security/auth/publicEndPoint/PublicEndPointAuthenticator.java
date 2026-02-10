package com.socialapp.sfll.security.auth.publicEndPoint;

import com.socialapp.sfll.security.auth.AuthResult;
import com.socialapp.sfll.security.auth.Authenticator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class PublicEndPointAuthenticator implements Authenticator {
    @Override
    public boolean supports(HttpServletRequest request) {
        // if no authentication method is found, this will be used as default
        return true;
    }

    @Override
    public AuthResult authenticate(HttpServletRequest request) {
         //check if the request starts with /api/public, if it does, return an authenticated result with no roles
        if (!request.getRequestURI().startsWith("/api/public")) {
            return new AuthResult(false, null);
        }

        // todo perform any additional checks if needed (e.g., rate limiting, IP blocking, etc.)

        return new AuthResult(true, null);
    }
}
