package com.app.gamereview.util.validation.validator;

import com.app.gamereview.enums.VoteType;
import com.app.gamereview.util.validation.annotation.ValidVoteType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class VoteTypeValidator implements ConstraintValidator<ValidVoteType, String> {

    @Override
    public boolean isValid(String providedType, ConstraintValidatorContext context) {
        if (providedType == null || providedType.isEmpty()) {
            return false;
        }

        for (VoteType voteType : VoteType.values()) {
            if (voteType.name().equals(providedType)) {
                return true;
            }
        }

        return false;
    }
}
