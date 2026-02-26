package com.socialapp.sfll.AuthService.security.auth.jwt;

import com.socialapp.sfll.AuthService.security.auth.AuthResult;
import com.socialapp.sfll.AuthService.security.auth.AuthUser;
import com.socialapp.sfll.AuthService.security.auth.Authenticator;
import com.socialapp.sfll.AuthService.security.auth.jwt.dto.JwtUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class JwtAuthenticator implements Authenticator {

    private final JwtTokenParser parser;

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

        // extract the token from the header
        String token = request.getHeader("Authorization").substring(7);

        // validate the token
        if (!parser.isValid(token)) {
            return new AuthResult(false, null);
        }

        // create jwt user details
        JwtUserDetails user = parser.extractUser(token);

        // convert to AuthUser
        AuthUser authUser = new AuthUser(
                user.id(),
                user.username(),
                Set.copyOf(user.roles())
        );

        // return successful authentication result
        return new AuthResult(true, authUser);
    }
}