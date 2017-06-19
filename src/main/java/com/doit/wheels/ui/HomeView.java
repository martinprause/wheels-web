package com.doit.wheels.ui;

import com.doit.wheels.dao.entities.User;
import com.doit.wheels.services.UserService;
import com.doit.wheels.services.impl.MessageByLocaleServiceImpl;
import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.List;

@Configurable
@SpringComponent
@SpringView(name = HomeView.NAME)
public class HomeView extends HorizontalLayout implements View{

    @Autowired
    private UserService userService;

    @Autowired
    private MessageByLocaleServiceImpl messageService;

    public static final String NAME = "home";
    private Grid<User> users = new Grid<>(User.class);
    private User user;
    private Binder<User> binder = new Binder<>(User.class);
    private TextField name = new TextField("Name");
    private TextField login = new TextField("Last name");
    private Button save = new Button("Save", e -> saveCustomer());
    private Label tableHeader = new Label();
    public void init(){

        updateGrid();
        users.setColumns("name", "login");
        users.addSelectionListener(e -> updateForm());
        tableHeader.setValue(messageService.getMessage("user.table"));
        addComponent(tableHeader);
        binder.bindInstanceFields(this);

        VerticalLayout layout = new VerticalLayout(tableHeader, users, name, login, save);
        addComponent(layout);
//        setContent(layout);
    }

    private void updateGrid() {
        List<User> userList = (List<User>) userService.findAll();
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
        name.setVisible(visible);
        login.setVisible(visible);
        save.setVisible(visible);
    }

    private void saveCustomer() {
        userService.saveUser(user);
        updateGrid();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        init();
    }

}
