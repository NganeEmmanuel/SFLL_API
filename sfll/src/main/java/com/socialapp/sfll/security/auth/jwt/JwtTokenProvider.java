package com.socialapp.sfll.security.auth.jwt;

import com.socialapp.sfll.exceptions.JwtException;
import com.socialapp.sfll.security.auth.AuthUser;
import com.socialapp.sfll.security.auth.jwt.dto.JwtToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Set;

@Component
public class JwtTokenProvider {
    @Autowired
    private JwtTokenParser parser;

    @Autowired
    private JwtTokenBlacklist jwtBlacklist;

    private final JwtProperties props;
    private final SecretKey key;

    public JwtTokenProvider(JwtProperties props) {
        this.props = props;
        byte[] decodedKey = Base64.getDecoder().decode(props.getSecret());

        this.key = Keys.hmacShaKeyFor(decodedKey);
    }

    /**
     * Generates a JWT token for the given authenticated user. The token includes claims such as the user's ID, username, and roles.
     * The token is signed using the HS256 algorithm and has an expiration time based on whether it's an access token or a refresh token.
     * @param user the authenticated user for whom the token is being generated
     * @param tokenTtl the duration for which the token should be valid (e.g., access token TTL or refresh token TTL)
     * @return a JWT token string that can be used for authentication and authorization in subsequent requests
     */
    private String generateToken(AuthUser user, Duration tokenTtl) {

        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuer(props.getIssuer())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(tokenTtl)))
                .claim("uid", user.getUserId())
                .claim("roles", user.getRoles())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Generates an access token for the given authenticated user.
     * The access token is used for authenticating API requests and has a shorter expiration time compared to refresh tokens.
     * @param user the authenticated user for whom the access token is being generated
     * @return a JWT access token string that can be used for authentication in subsequent API requests
     */
    public String generateAccessToken(AuthUser user) {
        return generateToken(user, props.getAccessTokenTtl());
    }

    /**
     * Generates a refresh token for the given authenticated user.
     * The refresh token is used to obtain new access tokens without requiring the user to re-authenticate.
     * @param user the authenticated user for whom the refresh token is being generated
     * @return a JWT refresh token string that can be used to obtain new access tokens when the current access token expires
     */
    public String generateRefreshToken(AuthUser user) {
        return generateToken(user, props.getRefreshTokenTtl());
    }

    /**
     * Generates both an access token and a refresh token for the given authenticated user.
     * This method is typically called after a successful authentication to provide the client with the necessary tokens for subsequent requests.
     * @param user the authenticated user for whom the tokens are being generated
     * @return a JwtToken object containing both the access token and the refresh token
     */
    public JwtToken generateTokens(AuthUser user) {
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);
        return JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Refreshes the access token using a valid refresh token. This method validates the provided refresh token, extracts the user details from it,
     * and generates a new access token for the user. If the refresh token is invalid, an exception is thrown.
     * @param refreshToken the JWT refresh token string that is used to obtain a new access token. This token must be valid and not expired for the method to succeed.
     * @return a JwtToken object containing a new access token and refresh token, allowing the client to continue making authenticated requests without re-authenticating the user.
     * If the refresh token is invalid, an exception is thrown indicating the failure.
     */
    public JwtToken refreshAccessToken(String refreshToken) {
        // Validate the refresh token and extract user details
        if (!parser.isValid(refreshToken)) {
            throw new JwtException("Invalid refresh token");
        }

        //Extract user details from the refresh token
        var jwtUserDetails = parser.extractUser(refreshToken);

        // Convert the extracted user details to an AuthUser object
        AuthUser user = new AuthUser(
                jwtUserDetails.id(),
                jwtUserDetails.username(),
                Set.copyOf(jwtUserDetails.roles())
        );

        // Generate a new access token for the user
        return generateTokens(user);
    }

    public void invalidateToken(String token) {
        // add to blacklist, etc. here if needed,
        // for example, use redis for temp store of blacklisted token and set eviction policy for token expiration date  + 5 mins
        Date expirationDate = parser.parse(token).getBody().getExpiration();
        jwtBlacklist.addTokenToBlacklist(token, expirationDate);
    }
}