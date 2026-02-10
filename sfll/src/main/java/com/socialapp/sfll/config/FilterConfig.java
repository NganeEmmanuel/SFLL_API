package com.socialapp.sfll.config;

import com.socialapp.sfll.ratelimit.InMemoryRateLimiter;
import com.socialapp.sfll.ratelimit.RateLimiter;
import com.socialapp.sfll.security.auth.Authenticator;
import com.socialapp.sfll.security.filter.AuthFilter;
import com.socialapp.sfll.security.filter.CorsFilter;
import com.socialapp.sfll.security.filter.RateLimitFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.DispatcherType;
import java.util.EnumSet;
import java.util.List;

/**
 * Central configuration class for registering HTTP servlet filters.
 *
 * <p>
 * This class defines the execution order and lifecycle of
 * all security-related filters in the application.
 * <p></p>
 * Filter Chain (Execution Order):
 *
 * <pre>
 * 1. CorsFilter       → Handles CORS and preflight requests
 * 2. RateLimitFilter  → Applies API rate limiting
 * 3. AuthFilter       → Performs authentication
 * </pre>
 *
 * All filters are applied to "/api/*" endpoints.
 */
@Configuration
@RequiredArgsConstructor
@SuppressWarnings("Null")
public class FilterConfig {

    /**
     * All registered authentication strategies.
     * <p>
     * Injected automatically by Spring from beans
     * implementing the Authenticator interface.
     * </p>
     */
    private final List<Authenticator> authenticators;

    /* =====================================================
       Infrastructure Beans
       ===================================================== */

    /**
     * Rate limiter implementation.
     * <p>
     * In-memory token bucket limiter.
     * Can be replaced later with Redis, Hazelcast, etc.
     * </p>
     */
    @Bean
    public RateLimiter rateLimiter() {
        return new InMemoryRateLimiter(
                4,   // Max tokens
                1    // Tokens per second
        );
    }

    /* =====================================================
       Filter Beans
       ===================================================== */

    /**
     * CORS filter.
     * <p>
     * Responsible for:
     * <pre>
     * - Setting CORS headers
     * - Handling preflight (OPTIONS) requests
     * </pre>
     * </p>
     *
     */
    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter();
    }

    /**
     * Rate limiting filter.
     * <p>
     * Blocks excessive requests based on client identity.
     * </p>
     */
    @Bean
    public RateLimitFilter rateLimitFilter(RateLimiter rateLimiter) {
        return new RateLimitFilter(rateLimiter);
    }

    /**
     * Authentication filter.
     * <p>
     * Delegates authentication to registered
     * Authenticator implementations.
     * </p>
     */
    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter(authenticators);
    }

    /* =====================================================
       Filter Registration
       ===================================================== */

    /**
     * Registers the CORS filter.
     * <p>
     * Executed first in the filter chain.
     * </p>
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistration(
            CorsFilter corsFilter
    ) {

        FilterRegistrationBean<CorsFilter> bean =
                new FilterRegistrationBean<>();

        bean.setFilter(corsFilter);
        bean.addUrlPatterns("/api/*");

        // Only intercept real HTTP requests
        bean.setDispatcherTypes(
                EnumSet.of(DispatcherType.REQUEST)
        );

        bean.setOrder(0); // FIRST

        return bean;
    }

    /**
     * Registers the rate limiting filter.
     * <p>
     * Executed after CORS and before authentication.
     * </p>
     */
    @Bean
    public FilterRegistrationBean<RateLimitFilter> rateLimitFilterRegistration(
            RateLimitFilter rateLimitFilter
    ) {

        FilterRegistrationBean<RateLimitFilter> bean =
                new FilterRegistrationBean<>();

        bean.setFilter(rateLimitFilter);
        bean.addUrlPatterns("/api/*");

        bean.setDispatcherTypes(
                EnumSet.of(DispatcherType.REQUEST)
        );

        bean.setOrder(1); // SECOND

        return bean;
    }

    /**
     * Registers the authentication filter.
     * <p>
     * Executed last in the security chain.
     * </p>
     */
    @Bean
    public FilterRegistrationBean<AuthFilter> authFilterRegistration(
            AuthFilter authFilter
    ) {

        FilterRegistrationBean<AuthFilter> bean =
                new FilterRegistrationBean<>();

        bean.setFilter(authFilter);
        bean.addUrlPatterns("/api/*");

        bean.setDispatcherTypes(
                EnumSet.of(DispatcherType.REQUEST)
        );

        bean.setOrder(2); // THIRD

        return bean;
    }
}