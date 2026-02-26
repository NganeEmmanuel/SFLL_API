package com.socialapp.sfll.AuthService.security.auth.jwt;

import com.socialapp.sfll.AuthService.security.auth.jwt.dto.JwtUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@SuppressWarnings("unchecked")
public class JwtTokenParser {

    private final SecretKey key;
    private final JwtProperties props;
    @Autowired
    private JwtTokenBlacklist jwtBlacklist;

    public JwtTokenParser(JwtProperties props) {
        this.props = props;
        byte[] decodedKey = Base64.getDecoder().decode(props.getSecret());

        this.key = Keys.hmacShaKeyFor(decodedKey);
    }

    /**
     * Parses the provided JWT token string, validates its signature and issuer, and returns the claims contained in the token.
     * @param token the JWT token string to be parsed and validated
     * @return a Jws<Claims> object containing the claims from the token if it is valid
     * @throws JwtException if the token is invalid, expired, or has an incorrect signature or issuer
     */
    public Jws<Claims> parse(String token)
            throws JwtException {

        // Use the JJWT library to parse the token, validate the signature and issuer
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .requireIssuer(props.getIssuer())
                .build()
                .parseClaimsJws(token);
    }

    /**
     * Checks if the provided JWT token is valid by attempting to parse it. If parsing
     * @param token the JWT token string
     * @return true if the token is valid, false otherwise
     */
    public boolean isValid(String token) {
        // check blacklist, etc. here if needed
        if(jwtBlacklist.isTokenBlacklisted(token)){
            return false;
        }
        // Attempt to parse the token. If parsing fails, it will throw a JwtException, which we catch to return false.
        try {
            parse(token);
            // check for expiration, etc. here if needed
            if(parse(token).getBody().getExpiration().before(new Date())){
                return false;
            }
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * Extracts user details from a valid JWT token. It retrieves the user ID, username, and roles
     * @param token the JWT token string
     * @return a JwtUserDetails object containing the extracted user information
     */
    public JwtUserDetails extractUser(String token) {
        // Parse the token to get the claims
        Claims claims = parse(token).getBody();

        // Extract user details from the claims
        Long id = claims.get("uid", Long.class);
        String username = claims.getSubject();

        // The roles claim is expected to be a List of Strings, so we retrieve it as such
        List<String> roles =
                claims.get("roles", List.class);

        // Create and return a JwtUserDetails object with the extracted information
        return new JwtUserDetails(id, username, roles);
    }
}