package com.doit.wheels.ui;

import com.doit.wheels.services.MessageByLocaleService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component // No SpringView annotation because this view can not be navigated to
@UIScope
public class AccessDeniedView extends VerticalLayout implements View {

    private final MessageByLocaleService messageByLocaleService;

    @Autowired
    public AccessDeniedView(MessageByLocaleService messageByLocaleService) {
        setMargin(true);
        Label accessDeniedLabel = new Label(messageByLocaleService.getMessage("accessDeniedView.label"));
        accessDeniedLabel.setId("accessDeniedView.label");
        accessDeniedLabel.addStyleName(ValoTheme.LABEL_FAILURE);
        accessDeniedLabel.setSizeUndefined();
        addComponent(accessDeniedLabel);
        this.messageByLocaleService = messageByLocaleService;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}
