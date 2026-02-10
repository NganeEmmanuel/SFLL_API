package com.socialapp.sfll.security.auth.jwt;

import com.socialapp.sfll.security.auth.AuthResult;
import com.socialapp.sfll.security.auth.AuthUser;
import com.socialapp.sfll.security.auth.Authenticator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * JWT Authenticator.
 * <p>
 * This implementation of the Authenticator interface uses JWT (JSON Web Token)
 * for authentication. It validates the token provided in the Authorization
 * header of the request.
 */
@Component
public class JwtAuthenticator implements Authenticator {

    /**
     * Checks if JWT authentication is supported for the given request.
     *
     * @param request the current HTTP request
     * @return true if the request uses Bearer token authentication, false otherwise
     */
    @Override
    public boolean supports(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        return auth != null && auth.startsWith("Bearer ");
    }

    /**
     * Authenticates the user using a JWT token.
     *
     * @param request the current HTTP request
     * @return an AuthResult object containing the authentication result
     */
    @Override
    public AuthResult authenticate(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);

        // TODO: Replace with real JWT validation later
        if ("valid-jwt-token".equals(token)) {
            return new AuthResult(
                    true,
                    new AuthUser(1, Set.of("USER"))
            );
        }

        return new AuthResult(false, null);
    }
}