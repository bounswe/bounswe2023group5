package com.app.gamereview.util.validation.annotation;

import com.app.gamereview.enums.TagType;
import com.app.gamereview.enums.VoteChoice;
import com.app.gamereview.util.validation.validator.VoteChoiceValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VoteChoiceValidator.class)
public @interface ValidVoteChoice {

    VoteChoice[] allowedValues();
    String message() default "Invalid VoteChoice, allowed values are: {allowedValues}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
