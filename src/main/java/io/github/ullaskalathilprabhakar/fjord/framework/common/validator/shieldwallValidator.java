package io.github.ullaskalathilprabhakar.fjord.framework.common.validator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import io.github.ullaskalathilprabhakar.fjord.framework.common.exception.ShieldwallConfigException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class shieldwallValidator implements ConstraintValidator<Shieldwall, CharSequence> {

    private long minLength;
    private long maxLength;
    private String permittedPattern;
    private String noPermittedPattern;
    private String message;

    private long minNumericValue;
    private long maxNumericValue;

    private boolean isNumericCheck;

    private boolean isCharLenCheck;

    @Autowired
    private Environment environment;

    @Override
    public void initialize(Shieldwall constraintAnnotation) {
        minLength = 0;
        maxLength = Long.MAX_VALUE;
        minNumericValue = Long.MIN_VALUE;
        maxNumericValue = Long.MAX_VALUE;

        if (!constraintAnnotation.maxLength().isEmpty() || !constraintAnnotation.minLength().isEmpty()) {
            this.isCharLenCheck = true;
        }

        if (!constraintAnnotation.maxNumericValue().isEmpty() || !constraintAnnotation.minNumericValue().isEmpty()) {
            this.isNumericCheck = true;
        }

        if (this.isCharLenCheck && this.isNumericCheck) {
            throw new ShieldwallConfigException("Cannot configure numeric check and char length check same time");
        }

        if (this.isCharLenCheck && !constraintAnnotation.minLength().isEmpty()) {
            String configKey = constraintAnnotation.minLength();
            String configValue = environment.getProperty(configKey);

            if (configValue == null) {
                throw new ShieldwallConfigException("No config value found for " + configKey);
            }

            try {
                this.minLength = Long.parseLong(configValue);
            } catch (NumberFormatException e) {
                throw new ShieldwallConfigException("Invalid numeric value for config key " + configKey, e);
            }
        }

        if (this.isCharLenCheck && !constraintAnnotation.maxLength().isEmpty()) {
            String configKey = constraintAnnotation.maxLength();
            String configValue = environment.getProperty(configKey);

            if (configValue == null) {
                throw new ShieldwallConfigException("No config value found for " + configKey);
            }

            try {
                this.maxLength = Long.parseLong(configValue);
            } catch (NumberFormatException e) {
                throw new ShieldwallConfigException("Invalid numeric value for config key " + configKey, e);
            }
        }

        // For minNumericValue
        if (this.isNumericCheck && !constraintAnnotation.minNumericValue().isEmpty()) {
            String configKey = constraintAnnotation.minNumericValue();
            String configValue = environment.getProperty(configKey);

            if (configValue == null) {
                throw new ShieldwallConfigException("No config value found for " + configKey);
            }

            try {
                this.minNumericValue = Long.parseLong(configValue);
            } catch (NumberFormatException e) {
                throw new ShieldwallConfigException("Invalid numeric value for config key " + configKey, e);
            }
        }

        // For maxNumericValue
        if (this.isNumericCheck && !constraintAnnotation.maxNumericValue().isEmpty()) {
            String configKey = constraintAnnotation.maxNumericValue();
            String configValue = environment.getProperty(configKey);

            if (configValue == null) {
                throw new ShieldwallConfigException("No config value found for " + configKey);
            }

            try {
                this.maxNumericValue = Long.parseLong(configValue);
            } catch (NumberFormatException e) {
                throw new ShieldwallConfigException("Invalid numeric value for config key " + configKey, e);
            }
        }

        if (!constraintAnnotation.message().isEmpty()) {
            String configKey = constraintAnnotation.message();
            String configValue = environment.getProperty(configKey);

            if (configValue == null || configValue.isEmpty()) {
                this.message = "Inavlid input data for the field";
            } else {
                this.message = configValue;
            }

        }

        this.permittedPattern = constraintAnnotation.permittedPattern();
        this.noPermittedPattern = constraintAnnotation.noPermittedPattern();

    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {

        if (value == null) {
            return true; // Null values should be handled by other constraints if needed
        }

        if (this.isCharLenCheck) {
            int length = value.length();

            if (length < minLength || length > maxLength) {
                addConstraintViolation(context, message);
                return false;
            }

        }

        if (this.isNumericCheck) {
            long longVal = 0;
            try {
                longVal = Long.parseLong(value.toString());
            } catch (NumberFormatException e) {
                addConstraintViolation(context, message);
                return false;
            }

            if (longVal < minNumericValue || longVal > maxNumericValue) {
                addConstraintViolation(context, message);
                return false;
            }

        }

        if (!permittedPattern.isEmpty() && !value.toString().matches(permittedPattern)) {
            addConstraintViolation(context, message);
            return false;
        }

        if (!noPermittedPattern.isEmpty() && value.toString().matches(noPermittedPattern)) {
            addConstraintViolation(context, message);
            return false;
        }

        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }

}
