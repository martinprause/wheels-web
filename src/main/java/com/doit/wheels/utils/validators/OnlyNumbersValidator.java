package com.doit.wheels.utils.validators;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.ValueContext;
import com.vaadin.data.validator.StringLengthValidator;

public class OnlyNumbersValidator extends StringLengthValidator {
    public OnlyNumbersValidator(String errorMessage, Integer minLength, Integer maxLength) {
        super(errorMessage, minLength, maxLength);
    }

    @Override
    public ValidationResult apply(String value, ValueContext context) {
        boolean isNumber = false;
        try {
            Integer.valueOf(value);
            isNumber = true;
        } catch (NumberFormatException e) {
            System.out.println("Isn't integer.");
        }
        try {
            Double.valueOf(value);
            isNumber = true;
        } catch (NumberFormatException e) {
            System.out.println("Isn't double.");
        }
        return super.toResult(value, isNumber);
    }
}
