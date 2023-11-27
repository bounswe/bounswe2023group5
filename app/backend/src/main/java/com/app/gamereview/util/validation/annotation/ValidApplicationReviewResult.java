package com.app.gamereview.util.validation.annotation;

import com.app.gamereview.enums.GroupApplicationReviewResult;
import com.app.gamereview.util.validation.validator.ApplicationReviewResultValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ApplicationReviewResultValidator.class)
public @interface ValidApplicationReviewResult {
    GroupApplicationReviewResult[] allowedValues();

    String message() default "Invalid Review Result, allowed values are: {allowedValues}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
