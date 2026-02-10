package com.socialapp.sfll.utils.validator;

import com.socialapp.sfll.exceptions.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class ValidatorImpl implements Validator {
    @Override
    public void validate(String input) {
        input = input.trim();
        // check for blank and enpty fileds
        if(input.isEmpty()) {
            throw new ValidationException("Fields should not be empty");
        }
    }
}
