package com.socialapp.sfll.security.auth.basic;

import com.socialapp.sfll.repository.ORMUserRopository;
import com.socialapp.sfll.security.auth.AuthResult;
import com.socialapp.sfll.security.auth.AuthUser;
import com.socialapp.sfll.security.auth.Authenticator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Set;

/**
 * Basic Authenticator.
 * <p>
 * This implementation of the Authenticator interface uses Basic Authentication.
 * It checks if the request has a valid Authorization header and attempts to
 * authenticate the user based on hardcoded credentials (for demo purposes).
 */
@Component
@RequiredArgsConstructor
public class BasicAuthenticator implements Authenticator {

    private final ORMUserRopository userRopository;

    /**
     * Checks if Basic Authentication is supported for the given request.
     *
     * @param request the current HTTP request
     * @return true if the request uses Basic Authentication, false otherwise
     */
    @Override
    public boolean supports(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        return auth != null && auth.startsWith("Basic ");
    }

    /**
     * Authenticates the user using Basic Authentication.
     *
     * @param request the current HTTP request
     * @return an AuthResult object containing the authentication result
     */
    @Override
    public AuthResult authenticate(HttpServletRequest request) {
        // read the header and extract the Base64-encoded credentials
        String header = request.getHeader("Authorization");
        String base64 = header.substring(6);

        // Decode the Base64-encoded credentials
        String decoded = new String(Base64.getDecoder().decode(base64));
        if (!decoded.contains(":")) {
            return new AuthResult(false, null);
        }

        String[] parts = decoded.split(":", 2);

        // Username and password extraction
        String username = parts[0];
        String password = parts[1];

        // TODO: Replace with real DB authentication later: DONE!!!
        // get user from database and check password
        var user = userRopository.findByUsername(username);

        // If user is not found or password does not match, return an unsuccessful AuthResult
        if (user.isEmpty() || !user.get().getPassword().equals(password)) {
            return new AuthResult(false, null);
        }

        // Create an AuthUser object with the user's details and return a successful AuthResult
        AuthUser authUser = new AuthUser(
                user.get().getId(),
                user.get().getUsername(),
                Set.of(user.get().getRole()));
        return new AuthResult(true, authUser);
    }
}