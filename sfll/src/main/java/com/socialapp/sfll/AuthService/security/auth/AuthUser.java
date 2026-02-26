package com.socialapp.sfll.AuthService.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUser {
    Long userId;
    private String username;
    private Set<String> roles;

    /**
     * Checks if a role is present in the set of roles for an AuthUser object
     * @param role string role you want to check for
     * @return true if present in the set and false otherwise
     */
    public boolean hasRole(String role) {
        return roles.contains(role);
    }
}
