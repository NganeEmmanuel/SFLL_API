package com.socialapp.sfll.AuthService.security.interceptor;

import jakarta.servlet.http.*;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * File Upload Interceptor.
 *
 * This interceptor checks the size of file uploads to ensure they do not
 * exceed a certain limit. If the size exceeds the limit, a RuntimeException
 * is thrown. Additional checks like file type and virus scans can be added.
 */
public class FileUploadInterceptor implements HandlerInterceptor {

    /**
     * Pre-handle method that checks the size of file uploads.
     *
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @param handler the chosen handler to execute
     * @return boolean indicating if the request should proceed
     * @throws RuntimeException if the file size exceeds the limit
     */
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        // Allow all non-POST requests to proceed without checks
        if (!request.getMethod().equals("POST")) {
            return true;
        }

        long size = request.getContentLengthLong();

        // Check if the file size exceeds the limit (5 MB in this case)
        if (size > 5_000_000) {
            throw new RuntimeException("File too large");
        }

        // Perform additional checks (file type, virus scan, etc.) here

        return true; // Proceed with the request handling
    }
}