package com.app.gamereview.util.validation.annotation;

import com.app.gamereview.enums.AchievementType;
import com.app.gamereview.util.validation.validator.AchievementTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AchievementTypeValidator.class)
public @interface ValidAchievementType {

    AchievementType[] allowedValues();
    String message() default "Invalid AchievementType, allowed values are: {allowedValues}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
