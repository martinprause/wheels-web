package com.doit.wheels.services.impl;

import com.doit.wheels.services.MessageByLocaleService;
import com.vaadin.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageByLocaleServiceImpl implements MessageByLocaleService {

    @Autowired
    MessageSource messageSource;

    @Override
    public String getMessage(String id) {
        Locale locale = VaadinSession.getCurrent().getLocale();
        return messageSource.getMessage(id, null, locale);
    }
}
