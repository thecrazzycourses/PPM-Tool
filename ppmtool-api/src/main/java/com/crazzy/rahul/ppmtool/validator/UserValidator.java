package com.crazzy.rahul.ppmtool.validator;

import com.crazzy.rahul.ppmtool.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        User user = (User) object;


        if (user.getPassword().length() < 6 ) {
            errors.rejectValue("password", "Length", "Password length should be min of 6 characters");
        }

        if (!user.getPassword().equals(user.getConfirmPassword()) ) {
            errors.rejectValue("confirmPassword", "Match", "Password & Confirm Password must match");
        }
    }
}
