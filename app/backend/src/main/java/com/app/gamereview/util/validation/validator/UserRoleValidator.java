package com.app.gamereview.util.validation.validator;


import com.app.gamereview.enums.UserRole;
import com.app.gamereview.util.validation.annotation.ValidUserRole;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserRoleValidator implements ConstraintValidator<ValidUserRole, String> {


    @Override
    public boolean isValid(String providedType, ConstraintValidatorContext context) {
        if (providedType == null || providedType.isEmpty()) {
            return false;
        }

        for (UserRole userRole : UserRole.values()) {
            if (userRole.name().equals(providedType)) {
                return true;
            }
        }

        return false;
    }
}
