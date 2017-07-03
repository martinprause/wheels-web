package com.doit.wheels.ui;

import com.doit.wheels.dao.entities.Country;
import com.doit.wheels.dao.entities.User;
import com.doit.wheels.services.CountryService;
import com.doit.wheels.services.UserService;
import com.doit.wheels.services.impl.MessageByLocaleServiceImpl;
import com.doit.wheels.utils.UserRoleEnum;
import com.doit.wheels.utils.exceptions.UserException;
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

//    @Autowired
//    private AccessLevelService accessLevelService;

    @Autowired
    private CountryService countryService;

    static final String NAME = "home";
    private Grid<User> users = new Grid<>(User.class);
//    private Grid<AccessLevel> accessLevels = new Grid<>(AccessLevel.class);
    private User user;
    private Binder<User> binderUser = new Binder<>(User.class);
//    private Binder<AccessLevel> binderAccessLevels = new Binder<>(AccessLevel.class);
    private TextField username = new TextField("Username");
    private TextField comment = new TextField("Comment");
    private Button save = new Button("Save", e -> saveCustomer());
    private Label userTableCaption = new Label();
    private Label accessTableCaption = new Label();
//    private ComboBox<AccessLevelType> accessEnums = new ComboBox<>("Accesses");
    private Button addAccess = new Button("Add", e -> addAccess());

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
//        VerticalLayout detailsLayout = new VerticalLayout(accessEnums, addAccess, username, comment, save);
        VerticalLayout detailsLayout = new VerticalLayout(addAccess, username, comment, save);
        addComponent(tableLayout);
        setExpandRatio(tableLayout, 1.0f);
//        accessLevels.setColumns("accessLevelType");
//        accessLevels.setWidth("80%");
        accessTableCaption.setId("access.table");
        accessTableCaption.setValue(messageService.getMessage("access.table"));
//        VerticalLayout accessesLayout = new VerticalLayout(accessTableCaption, accessLevels);
        VerticalLayout accessesLayout = new VerticalLayout(accessTableCaption);
        addComponent(accessesLayout);
        setExpandRatio(accessesLayout, 1.0f);
        addComponent(detailsLayout);
        setExpandRatio(detailsLayout, 0.4f);
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
//            accessLevels.setItems(user.getAccessLevels());
//            accessEnums.setItems(AccessLevelType.values());
        }
    }

    private void setFormVisible(boolean visible) {
        username.setVisible(visible);
        comment.setVisible(visible);
        save.setVisible(visible);
//        accessLevels.setVisible(visible);
//        accessEnums.setVisible(visible);
        accessTableCaption.setVisible(visible);
        addAccess.setVisible(visible);
    }

    private void saveCustomer() {
        try {
            userService.saveUser(user);
        } catch (UserException e) {
            e.printStackTrace();
        }
        updateGrid();
    }

    private void addAccess(){
        Country country1 = new Country();
        country1.setDescription("Ukraine");
        countryService.saveCountry(country1);


        User user = new User();
        user.setUsername("driver");
        user.setPassword("p");
        user.setRole(UserRoleEnum.DRIVER);
        user.setCountry(country1);
        try {
            userService.saveUser(user);
        } catch (UserException e) {
            e.printStackTrace();
        }

//        AccessLevel accessLevel = accessLevelService.findAccessLevelByAccessLevelType(accessEnums.getSelectedItem().get());
//        user.addAccessLevel(accessLevel);
////        accessLevel.getUsers().add(user);
//        userService.saveUser(user);
//        updateForm();
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        init();
    }

}
