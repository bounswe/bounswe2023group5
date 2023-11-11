package com.app.gamereview.util.validation.validator;

import com.app.gamereview.enums.SortDirection;
import com.app.gamereview.util.validation.annotation.ValidSortDirection;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SortDirectionValidator implements ConstraintValidator<ValidSortDirection, String> {
    @Override
    public boolean isValid(String providedDirection, ConstraintValidatorContext context) {
        if (providedDirection == null || providedDirection.isEmpty()) {
            return false;
        }

        for (SortDirection sortDirection : SortDirection.values()) {
            if (sortDirection.name().equals(providedDirection)) {
                return true;
            }
        }

        return false;
    }
}
