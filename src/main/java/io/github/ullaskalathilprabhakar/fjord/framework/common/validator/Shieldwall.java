package io.github.ullaskalathilprabhakar.fjord.framework.common.validator;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = shieldwallValidator.class)
public @interface Shieldwall {

    String message() default "Invalid input";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String minLength() default "";

    String maxLength() default "";

    String minNumericValue() default "";

    String maxNumericValue() default "";

    String permittedPattern() default "";

    String noPermittedPattern() default "";
}
