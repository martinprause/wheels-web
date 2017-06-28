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


    static final String NAME = "home";
    private Grid<User> users = new Grid<>(User.class);
    private User user;
    private Binder<User> binderUser = new Binder<>(User.class);
    private TextField username = new TextField("Username");
    private TextField comment = new TextField("Comment");
    private Button save = new Button("Save", e -> saveCustomer());
    private Label userTableCaption = new Label();
    private Label accessTableCaption = new Label();

    private void init(){
        setWidth("100%");
        updateGrid();
        users.setColumns("username", "role", "comment");
        users.addSelectionListener(e -> updateForm());
        userTableCaption.setId("user.table");
        userTableCaption.setValue(messageService.getMessage("user.table"));
        addComponent(userTableCaption);
        binderUser.bindInstanceFields(this);
        VerticalLayout tableLayout = new VerticalLayout(userTableCaption, users);
        addComponent(tableLayout);
//        setExpandRatio(tableLayout, 1.0f);
        accessTableCaption.setId("access.table");
        accessTableCaption.setValue(messageService.getMessage("access.table"));
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
            binderUser.setBean(user);
            setFormVisible(true);
        }
    }

    private void setFormVisible(boolean visible) {
        username.setVisible(visible);
        comment.setVisible(visible);
        save.setVisible(visible);
        accessTableCaption.setVisible(visible);
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
