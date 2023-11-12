package com.app.gamereview.util.validation.validator;

import com.app.gamereview.enums.SortType;
import com.app.gamereview.util.validation.annotation.ValidSortType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SortTypeValidator implements ConstraintValidator<ValidSortType, String> {
    @Override
    public boolean isValid(String providedType, ConstraintValidatorContext context) {
        if (providedType == null || providedType.isEmpty()) {
            return false;
        }

        for (SortType sortType : SortType.values()) {
            if (sortType.name().equals(providedType)) {
                return true;
            }
        }

        return false;
    }
}
