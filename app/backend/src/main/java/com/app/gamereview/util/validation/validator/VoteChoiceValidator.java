package com.app.gamereview.util.validation.validator;

import com.app.gamereview.enums.VoteChoice;
import com.app.gamereview.util.validation.annotation.ValidVoteChoice;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class VoteChoiceValidator implements ConstraintValidator<ValidVoteChoice, String> {

    @Override
    public boolean isValid(String providedChoice, ConstraintValidatorContext context) {
        if (providedChoice == null || providedChoice.isEmpty()) {
            return false;
        }

        for (VoteChoice voteChoice : VoteChoice.values()) {
            if (voteChoice.name().equals(providedChoice)) {
                return true;
            }
        }

        return false;
    }
}
