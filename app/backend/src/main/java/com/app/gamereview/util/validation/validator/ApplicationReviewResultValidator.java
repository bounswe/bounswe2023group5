package com.app.gamereview.util.validation.validator;

import com.app.gamereview.enums.GroupApplicationReviewResult;
import com.app.gamereview.util.validation.annotation.ValidApplicationReviewResult;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ApplicationReviewResultValidator implements ConstraintValidator<ValidApplicationReviewResult, String> {

    @Override
    public boolean isValid(String providedResult, ConstraintValidatorContext context) {
        if (providedResult == null || providedResult.isEmpty()) {
            return false;
        }

        for (GroupApplicationReviewResult reviewResult : GroupApplicationReviewResult.values()) {
            if (reviewResult.name().equals(providedResult)) {
                return true;
            }
        }

        return false;
    }
}
