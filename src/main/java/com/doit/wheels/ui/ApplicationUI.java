package com.doit.wheels.ui;

import com.doit.wheels.dao.repositories.UserRepository;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;


@Viewport("user-scalable=no,initial-scale=1.0")
@Theme("mytheme")
@SpringUI
public class ApplicationUI extends UI implements View{

    @Autowired
    private UserRepository service;

    @Autowired
    private SpringViewProvider viewProvider;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        new Navigator(this, this);
        getNavigator().addProvider(viewProvider);
        Responsive.makeResponsive(this);
        getPage().setTitle("Wheels");
        getSession().setLocale(Locale.ENGLISH);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

}
