package com.socialapp.sfll.security.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Authentication Result.
 * <p>
 * This class represents the result of an authentication attempt
 * containing information about whether the authentication was successful,
 * the user ID, and the roles assigned to the user.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResult {
    private boolean authenticated;
    private AuthUser auth;

}