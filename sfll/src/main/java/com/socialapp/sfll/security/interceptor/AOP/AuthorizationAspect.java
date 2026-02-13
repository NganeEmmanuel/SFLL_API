package com.socialapp.sfll.security.interceptor.AOP;

import com.socialapp.sfll.annotation.RequireRole;
import com.socialapp.sfll.exceptions.ForbiddenException;
import com.socialapp.sfll.security.auth.AuthResult;
import com.socialapp.sfll.security.context.AuthContext;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AuthorizationAspect {

    @Before("@annotation(requireRole)")
    public void check(RequireRole requireRole) {

        AuthResult auth = AuthContext.get();

        if (auth == null || !auth.getAuth().hasRole(requireRole.value())) {
            throw new ForbiddenException("Authorization Required");
        }
    }
}