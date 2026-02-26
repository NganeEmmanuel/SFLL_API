package com.socialapp.sfll.AuthService.security.auth.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@ConfigurationProperties(prefix = "security.jwt")
@Data
public class JwtProperties {

    private String secret;
    private String issuer;
    private Duration accessTokenTtl;
    private Duration refreshTokenTtl;

    // getters/setters
}