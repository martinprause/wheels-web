package com.doit.wheels.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.stereotype.Component;

@Component // No SpringView annotation because this view can not be navigated to
@UIScope
public class AccessDeniedView extends VerticalLayout implements View {

    public AccessDeniedView() {
        setMargin(true);
        Label lbl = new Label("You don't have access to this view.");
        lbl.addStyleName(ValoTheme.LABEL_FAILURE);
        lbl.setSizeUndefined();
        Button logout = new Button("logout");
        logout.addClickListener(e -> logout());
        addComponent(lbl);
        addComponent(logout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

    private void logout() {
        getUI().getPage().reload();
        getSession().close();
    }
}
