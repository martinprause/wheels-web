package com.doit.wheels.ui;

import com.doit.wheels.dao.entities.User;
import com.doit.wheels.services.UserService;
import com.doit.wheels.services.impl.MessageByLocaleServiceImpl;
import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Locale;

@Configurable
@SpringComponent
@SpringView(name = HomeView.NAME)
public class HomeView extends HorizontalLayout implements View{

    @Autowired
    private UserService userService;

    @Autowired
    private MessageByLocaleServiceImpl messageService;

    public static final String NAME = "";
    private Grid<User> users = new Grid<>(User.class);
    private User user;
    private Binder<User> binder = new Binder<>(User.class);
    private TextField username = new TextField("Username");
    private TextField password = new TextField("Password");
    private Button save = new Button("Save", e -> saveCustomer());
    private Label tableHeader = new Label();
    private Label currentUser = new Label();
    private Button changeLocale;
    private Button logout;


    public void init(){
        updateGrid();
        users.setColumns("username", "password");
        users.addSelectionListener(e -> updateForm());
        tableHeader.setId("user.table");
        tableHeader.setValue(messageService.getMessage("user.table"));
        currentUser.setValue(SecurityContextHolder.getContext().getAuthentication().getName());
        addComponent(tableHeader);
        binder.bindInstanceFields(this);
        changeLocale = new Button(messageService.getMessage("localization"));
        changeLocale.setId("localization");
        changeLocale.addClickListener(e -> {
            Locale locale = !VaadinSession.getCurrent().getLocale().equals(Locale.ENGLISH) ? Locale.ENGLISH : Locale.GERMAN;
                messageService.updateLocale(getUI(), locale);
                VaadinSession.getCurrent().setLocale(locale);
        });
        logout = new Button("logout");
        logout.addClickListener(e -> logout());
        VerticalLayout layout = new VerticalLayout(tableHeader, users, username, password, save, changeLocale, currentUser, logout);
        addComponent(layout);
    }

    private void updateGrid() {
        List<User> userList = userService.findAll();
        users.setItems(userList);
        setFormVisible(false);
    }

    private void updateForm() {
        if (users.asSingleSelect().isEmpty()) {
            setFormVisible(false);
        } else {
            user = users.asSingleSelect().getValue();
            binder.setBean(user);
            setFormVisible(true);
        }
    }

    private void setFormVisible(boolean visible) {
        username.setVisible(visible);
        password.setVisible(visible);
        save.setVisible(visible);
    }

    private void saveCustomer() {
        userService.saveUser(user);
        updateGrid();
    }

    private void logout() {
        getUI().getPage().reload();
        getSession().close();
    }
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        init();
    }

}
