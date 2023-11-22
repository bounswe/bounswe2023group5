package com.app.gamereview.util.validation.validator;

import com.app.gamereview.enums.AchievementType;
import com.app.gamereview.util.validation.annotation.ValidAchievementType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AchievementTypeValidator implements ConstraintValidator<ValidAchievementType, String> {

    @Override
    public boolean isValid(String providedType, ConstraintValidatorContext context) {
        if (providedType == null || providedType.isEmpty()) {
            return false;
        }

        for (AchievementType achievementType : AchievementType.values()) {
            if (achievementType.name().equals(providedType)) {
                return true;
            }
        }

        return false;
    }
}
