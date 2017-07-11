package com.doit.wheels.services.impl;

import com.doit.wheels.services.MessageByLocaleService;
import com.doit.wheels.utils.UTF8Control;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Consumer;

@Component
public class MessageByLocaleServiceImpl implements MessageByLocaleService {

    @Autowired
    MessageSource messageSource;

    @Override
    public String getMessage(String id) {

        Locale locale = null;
        try {
            locale = VaadinSession.getCurrent().getLocale();
        } catch (NullPointerException e) {
            locale = Locale.ENGLISH;
        }
        try {
            return new String(messageSource.getMessage(id, null, locale).getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    @Override
    public void updateLocale(final HasComponents ui, Locale locale) {
        final ResourceBundle rb = ResourceBundle.getBundle("locale/messages", locale, new UTF8Control());
        // locale may not be null, howvever the current UI Locale may be null!
        if (locale.equals(VaadinSession.getCurrent().getLocale())) {
            return;
        }
        walkComponentTree(ui, c -> {
            final String id = c.getId();
            if (id == null) {
                return;
            }
            if (c instanceof Label){
                ((Label) c).setValue(rb.getString(c.getId()));
            }
            else if (c instanceof TextField){
                ((TextField) c).setPlaceholder(rb.getString(c.getId()));
            }
//            else if (c instanceof PasswordField){
//                ((PasswordField) c).setPlaceholder(rb.getString(c.getId()));
//            }
            else if(c instanceof Grid){
                ((Grid) c).getDefaultHeaderRow().getComponents().forEach(o -> {
                    if(o.getId() != null)
                        o.setCaption(rb.getString(o.getId()));
                });
            }
            else if (c instanceof Button){
                c.setCaption(rb.getString(c.getId()));
            }
            else if (c instanceof CheckBox){
                c.setCaption(rb.getString(c.getId()));
            }
            else if (c instanceof Notification){
                c.setCaption(rb.getString(c.getStyleName()));
            }
        });
    }

    // recursively walk the Component true
    private static void walkComponentTree(com.vaadin.ui.Component c, Consumer<com.vaadin.ui.Component> visitor) {
        visitor.accept(c);
        if (c instanceof HasComponents) {
            for (com.vaadin.ui.Component child : ((HasComponents)c)) {
                walkComponentTree(child, visitor);
            }
        }
    }

}
