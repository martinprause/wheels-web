package com.doit.wheels.services;

import com.vaadin.ui.HasComponents;

import java.util.Locale;

public interface MessageByLocaleService {

   String getMessage(String id);

   void updateLocale(final HasComponents ui, Locale locale);
}
