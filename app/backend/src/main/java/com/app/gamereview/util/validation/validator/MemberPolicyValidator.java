package com.app.gamereview.util.validation.validator;


import com.app.gamereview.enums.MembershipPolicy;
import com.app.gamereview.util.validation.annotation.ValidMemberPolicy;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MemberPolicyValidator implements ConstraintValidator<ValidMemberPolicy, String> {


    @Override
    public boolean isValid(String providedType, ConstraintValidatorContext context) {
        if (providedType == null || providedType.isEmpty()) {
            return false;
        }

        for (MembershipPolicy membershipPolicy : MembershipPolicy.values()) {
            if (membershipPolicy.name().equals(providedType)) {
                return true;
            }
        }

        return false;
    }
}
