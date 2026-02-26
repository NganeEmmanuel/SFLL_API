package com.socialapp.sfll.UserService.annotation;

import com.socialapp.sfll.UserService.annotation.AnnotaionImpl.NotContainsSpecialCharValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {NotContainsSpecialCharValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotContainsSpecialChar {
    String message() default "Field is either null, empty or contains special characters";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
