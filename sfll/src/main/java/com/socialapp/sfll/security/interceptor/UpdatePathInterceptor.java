package com.socialapp.sfll.security.interceptor;

import com.socialapp.sfll.security.context.AuthContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class UpdatePathInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Check if the request is a PUT request to an update endpoint
        var authResult = AuthContext.get();

        // Not authenticated
        if (authResult == null || authResult.getAuth() == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        if (request.getMethod().equals("PUT")) {
            if(authResult.getAuth().hasRole("ADMIN") || authResult.getAuth().hasRole("USER")) {
                return true;
            }
        }

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("""
            {
              "error":"UNAUTHORIZED",
              "message":"You do not have permission to perform this action"
            }
            """);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, org.springframework.web.servlet.ModelAndView modelAndView) throws Exception {
        // No post-processing needed for this interceptor
        // INCLUDE LOGGING OR OTHER POST-HANDLING LOGIC IF NECESSARY
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // No after-completion processing needed for this interceptor
        // INCLUDE CLEANUP OR OTHER LOGIC IF NECESSARY
    }
}
