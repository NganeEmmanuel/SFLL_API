package com.socialapp.sfll.UserService.annotation.AnnotaionImpl;

import com.socialapp.sfll.UserService.annotation.NotContainsSpecialChar;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class NotContainsSpecialCharValidator implements ConstraintValidator<NotContainsSpecialChar, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        // Check if the string contains any special characters
        return value.matches("^[a-zA-Z0-9]*$");
    }


}
