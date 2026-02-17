package com.socialapp.sfll.config;

import com.socialapp.sfll.security.interceptor.*;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;


/**
 * Interceptor Configuration Class.
 * <p>
 * This configuration class sets up various interceptors for handling
 * authentication, authorization, and file uploads within the application.
 * It configures specific paths for each interceptor.
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * Add custom interceptors to the application's interceptor registry.
     *
     * @param registry the interceptor registry to which interceptors can be added
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // Add RoleInterceptor to handle admin-specific routes
        registry.addInterceptor(new RoleInterceptor("ADMIN"))
                .addPathPatterns("/api/admin/**");

        registry.addInterceptor(new UpdatePathInterceptor())
                .addPathPatterns("/api/public/v1/users/update");

        // Add FileUploadInterceptor to handle file upload routes
        registry.addInterceptor(new FileUploadInterceptor())
                .addPathPatterns("/api/files/**");
    }
}