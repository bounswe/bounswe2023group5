package com.app.gamereview.util.validation.validator;

import com.app.gamereview.enums.TagType;
import com.app.gamereview.util.validation.annotation.ValidTagType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TagTypeValidator implements ConstraintValidator<ValidTagType, String> {

    @Override
    public boolean isValid(String providedType, ConstraintValidatorContext context) {
        if (providedType == null || providedType.isEmpty()) {
            return false;
        }

        for (TagType tagType : TagType.values()) {
            if (tagType.name().equals(providedType)) {
                return true;
            }
        }

        return false;
    }
}
