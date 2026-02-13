package com.socialapp.sfll.security.auth.jwt.dto;

import java.util.List;

/**
 * A record that represents the details of a user for JWT authentication.
 * It contains the user's ID, username, and a list of roles associated with the user.
 */
public record JwtUserDetails(
        Long id,
        String username,
        List<String> roles
) {}