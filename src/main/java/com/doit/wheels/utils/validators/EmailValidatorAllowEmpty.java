package com.doit.wheels.utils.validators;

import com.vaadin.data.validator.RegexpValidator;

public class EmailValidatorAllowEmpty extends RegexpValidator {
    private static final String PATTERN = "^$|^([a-zA-Z0-9_\\.\\-+])+@[a-zA-Z0-9-.]+\\.[a-zA-Z0-9-]{2,}$";

    public EmailValidatorAllowEmpty(String errorMessage) {
        super(errorMessage, PATTERN, true);
    }
}
