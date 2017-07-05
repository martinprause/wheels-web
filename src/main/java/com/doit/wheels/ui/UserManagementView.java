package com.doit.wheels.ui;

import com.doit.wheels.dao.entities.AccessLevel;
import com.doit.wheels.dao.entities.Country;
import com.doit.wheels.dao.entities.User;
import com.doit.wheels.services.AccessLevelService;
import com.doit.wheels.services.CountryService;
import com.doit.wheels.services.UserService;
import com.doit.wheels.services.impl.MessageByLocaleServiceImpl;
import com.doit.wheels.utils.AccessLevelType;
import com.doit.wheels.utils.UserRoleEnum;
import com.doit.wheels.utils.exceptions.UserException;
import com.doit.wheels.utils.validators.EmailValidatorAllowEmpty;
import com.vaadin.annotations.PropertyId;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Configurable
@SpringComponent
@SpringView(name = "user-management")
public class UserManagementView extends VerticalLayout implements View {

    private static String mandatory = ":*";

    @Autowired
    MessageByLocaleServiceImpl messageService;

    @Autowired
    UserService userService;

    @Autowired
    CountryService countryService;

    @Autowired
    AccessLevelService accessLevelService;

    private CssLayout menubar;

    private VerticalLayout userCreateDataWrapper;

    private HorizontalLayout userCreateData;

    private VerticalLayout userCreateDataBlockLeft;

    private VerticalLayout userCreateDataBlockRight;

    private HorizontalLayout userCreateFooterLayout;

    private HorizontalLayout userAccessWrapper;

    @PropertyId("username")
    private final TextField loginName = new TextField();

    private final TextField password = new PasswordField();

    @PropertyId("employeeNo")
    private final TextField employeeNumber = new TextField();

    private final ComboBox<UserRoleEnum> role = new ComboBox<>();

    private final TextField email = new TextField();

    private final TextField phone = new TextField();

    private final TextField mobile = new TextField();

    private final TextField firstname = new TextField();

    private final TextField lastname = new TextField();

    private final TextField address1 = new TextField();

    private final TextField address2 = new TextField();

    private final TextField zipCode = new TextField();

    private final TextField city = new TextField();

    private final ComboBox<Country> country = new ComboBox<>();

    private final TextField comment = new TextField();

    private User user;
    Binder<User> binder;

    Button editUserButton;
    Button deleteUserButton;

    Grid<User> userGrid;

    private HashMap<AccessLevelType, CheckBox> accessCheckboxes;

    private CheckBox deleteOrderCheckbox;
    private CheckBox createOrderCheckBox;
    private CheckBox createUserCheckbox;
    private CheckBox deleteUserCheckbox;
    private CheckBox reportsCheckbox;
    private Button saveAccessButton;

    private void init(){
        setSizeFull();
        menubar = new CssLayout();
        menubar.addStyleName("user-management-menubar");
        Button createUserButton = new Button(messageService.getMessage("userManagement.createUser"));
        createUserButton.setId("userManagement.createUser");
        createUserButton.addStyleName("create-user-button");
        createUserButton.setIcon(new ThemeResource("img/ico/new-user.png"));
        createUserButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        createUserButton.addClickListener(e -> {
            user = new User();
            binder.setBean(user);
            userAccessWrapper.setVisible(false);
            userCreateDataWrapper.setVisible(true);
        });
        createUserButton.addStyleName("clear-button");

        Button manageUserAccessButton = new Button(messageService.getMessage("userManagement.manageUserAccess"));
        manageUserAccessButton.setId("userManagement.manageUserAccess");
        manageUserAccessButton.addStyleName("manage-user-access-button");
        manageUserAccessButton.setIcon(new ThemeResource("img/ico/settings.png"));
        manageUserAccessButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        manageUserAccessButton.addStyleName("clear-button");
        manageUserAccessButton.addClickListener(e -> {
            userCreateDataWrapper.setVisible(false);
            userAccessWrapper.setVisible(true);
        });

        menubar.addComponent(createUserButton);
        menubar.addComponent(manageUserAccessButton);
        this.addComponent(menubar);
        initUserCreateView();
        initUserAccessWrapper();
    }

    private void initUserAccessWrapper(){
        userAccessWrapper = new HorizontalLayout();
        userAccessWrapper.setWidth("90%");
        userAccessWrapper.setHeight("100%");
        userAccessWrapper.addStyleName("user-access");
        this.addComponent(userAccessWrapper);
        userAccessWrapper.setVisible(false);

        VerticalLayout gridWrapper = new VerticalLayout();
        gridWrapper.setWidth("100%");
        gridWrapper.setHeight("100%");

        Panel panel = new Panel();
        panel.setSizeFull();
        panel.addStyleName("user-access-user-list");

        userGrid = new Grid<>(User.class);
        userGrid.addSelectionListener(e -> selectUserFromGrid());
        userGrid.setItems(userService.findAll());

//        userGrid.setCaption("sdfsdf");
        userGrid.setColumns("username", "firstname", "lastname", "email", "phone", "mobile");
        userGrid.setSizeFull();
        userGrid.setId("userManagement.userlist.grid");
//        userGrid.getDefaultHeaderRow().getCell("username").setComponent(new Label(""));
        panel.setContent(userGrid);
        gridWrapper.addComponent(panel);

        HorizontalLayout userListButtonLayout = new HorizontalLayout();

        editUserButton = new Button();
        editUserButton.setCaption("Edit user");
        editUserButton.addClickListener(e -> addAccesses());
        userListButtonLayout.addComponent(editUserButton);

        deleteUserButton = new Button();
        deleteUserButton.setCaption("Delete User");
        deleteUserButton.addClickListener(e -> deleteUser());
        userListButtonLayout.addComponent(deleteUserButton);
        gridWrapper.addComponent(userListButtonLayout);

        userAccessWrapper.addComponent(gridWrapper);

        VerticalLayout accessesWrapper = new VerticalLayout();
        accessesWrapper.setWidth("20%");
        accessesWrapper.addStyleName("accesses-wrapper");

        deleteOrderCheckbox = new CheckBox("Delete Order");
        createOrderCheckBox = new CheckBox("Create Order");
        createUserCheckbox = new CheckBox("Create User");
        deleteUserCheckbox = new CheckBox("Delete User");
        reportsCheckbox = new CheckBox("Reports");

        accessCheckboxes = new HashMap<>();
        accessCheckboxes.put(AccessLevelType.DeleteOrder, deleteOrderCheckbox);
        accessCheckboxes.put(AccessLevelType.CreateOrder, createOrderCheckBox);
        accessCheckboxes.put(AccessLevelType.CreateUser, createUserCheckbox);
        accessCheckboxes.put(AccessLevelType.DeleteUser, deleteUserCheckbox);
        accessCheckboxes.put(AccessLevelType.Reports, reportsCheckbox);

        saveAccessButton = new Button();
        saveAccessButton.setCaption("Save access rights");
        saveAccessButton.addClickListener(e -> saveAccesses());

        accessesWrapper.addComponent(deleteOrderCheckbox);
        accessesWrapper.addComponent(createOrderCheckBox);
        accessesWrapper.addComponent(createUserCheckbox);
        accessesWrapper.addComponent(deleteUserCheckbox);
        accessesWrapper.addComponent(reportsCheckbox);
        accessesWrapper.addComponent(saveAccessButton);
        userAccessWrapper.addComponent(accessesWrapper);

    }

    private void saveAccesses() {
        Set<AccessLevel> accessLevels = new HashSet<>();
        for (AccessLevelType accessLevelType : accessCheckboxes.keySet()) {
            AccessLevel accessLevel = accessLevelService.findAccessLevelByAccessLevel(accessLevelType);
            if (accessCheckboxes.get(accessLevelType).getValue().equals(Boolean.TRUE)){
                accessLevel.getUsers().add(user);
                accessLevels.add(accessLevel);
            } else {
                accessLevel.getUsers().remove(user);
                accessLevelService.save(accessLevel);
            }
        }
        user.setAccesses(accessLevels);
        userService.updateUser(user);
    }

    private void deleteUser() {
        if (user != null){

        }
    }

    private void selectUserFromGrid(){
        resetCheckboxes();
        user = userGrid.asSingleSelect().getValue();
        Set<AccessLevel> accessLevels = new HashSet<>(user.getAccesses());
        if (accessLevels.size() != 0){
            for (AccessLevel accessLevel : accessLevels){
                accessCheckboxes.get(accessLevel.getAccessLevel()).setValue(true);
            }

        }
    }

    private void resetCheckboxes(){
        for (CheckBox checkBox : accessCheckboxes.values()) {
            checkBox.setValue(false);
        }
    }

    private void addAccesses(){
//        User user = userService.findUserByUsername("user");
        AccessLevel accessLevel = accessLevelService.findAccessLevelByAccessLevel(AccessLevelType.CreateUser);
        user.getAccesses().remove(accessLevel);
        accessLevel.getUsers().remove(user);
        userService.updateUser(user);
    }

    private void initUserCreateView(){
        userCreateDataWrapper = new VerticalLayout();
        userCreateDataWrapper.setSizeFull();
        userCreateDataWrapper.addStyleName("user-management");

        userCreateData = new HorizontalLayout();
        userCreateData.addStyleName("user-management-data-horizontal");

        userCreateDataBlockLeft = new VerticalLayout();
        userCreateDataBlockLeft.addStyleName("user-management-data-left");
        userCreateData.addComponent(userCreateDataBlockLeft);

        userCreateDataBlockRight = new VerticalLayout();
        userCreateDataBlockRight.addStyleName("user-management-data-right");
        userCreateData.addComponent(userCreateDataBlockRight);
        userCreateDataWrapper.addComponent(userCreateData);
        this.addComponent(userCreateDataWrapper);
        initUserCreateLeftPart();
        initUserCreateRightPart();
        initCreateUserFooter();
        initBinderAndValidation();
    }

    private void initUserCreateLeftPart(){
        HorizontalLayout employeeNumberLayout = new HorizontalLayout();
        Label employeeNumberLabel = new Label(messageService.getMessage("userManagement.emoloyeeNumber.label") + mandatory);
        employeeNumberLabel.addStyleName("user-management-label");
        employeeNumberLabel.setId("userManagement.emoloyeeNumber.label");
        employeeNumberLayout.addComponents(employeeNumberLabel, employeeNumber);
        userCreateDataBlockLeft.addComponent(employeeNumberLayout);

        HorizontalLayout loginNameLayout = new HorizontalLayout();
        Label loginNameLabel = new Label(messageService.getMessage("userManagement.loginName.label") + mandatory);
        loginNameLabel.addStyleName("user-management-label");
        loginNameLabel.setId("userManagement.loginName.label");
        loginNameLayout.addComponents(loginNameLabel, loginName);
        userCreateDataBlockLeft.addComponent(loginNameLayout);

        HorizontalLayout passwordLayout = new HorizontalLayout();
        Label passwordLabel = new Label(messageService.getMessage("userManagement.password.label") + mandatory);
        passwordLabel.addStyleName("user-management-label");
        passwordLabel.setId("userManagement.password.label");
        passwordLayout.addComponents(passwordLabel, password);
        userCreateDataBlockLeft.addComponent(passwordLayout);

        HorizontalLayout roleLayout = new HorizontalLayout();
        Label roleLabel = new Label(messageService.getMessage("userManagement.role.label") + mandatory);
        roleLabel.addStyleName("user-management-label");
        roleLabel.setId("userManagement.role.label");
        roleLayout.addComponents(roleLabel, role);
        userCreateDataBlockLeft.addComponent(roleLayout);

        HorizontalLayout emailLayout = new HorizontalLayout();
        Label emailLabel = new Label(messageService.getMessage("userManagement.email.label"));
        emailLabel.addStyleName("user-management-label");
        emailLabel.setId("userManagement.email.label");
        emailLayout.addComponents(emailLabel, email);
        userCreateDataBlockLeft.addComponent(emailLayout);

        HorizontalLayout phoneLayout = new HorizontalLayout();
        Label phoneLabel = new Label(messageService.getMessage("userManagement.phone.label"));
        phoneLabel.addStyleName("user-management-label");
        phoneLabel.setId("userManagement.phone.label");
        phoneLayout.addComponents(phoneLabel, phone);
        userCreateDataBlockLeft.addComponent(phoneLayout);

        HorizontalLayout mobileLayout = new HorizontalLayout();
        Label mobileLabel = new Label(messageService.getMessage("userManagement.mobile.label"));
        mobileLabel.addStyleName("user-management-label");
        mobileLabel.setId("userManagement.mobile.label");
        mobileLayout.addComponents(mobileLabel, mobile);
        userCreateDataBlockLeft.addComponent(mobileLayout);

        role.setItems(UserRoleEnum.values());
    }

    private void initUserCreateRightPart(){
        HorizontalLayout firstnameLayout = new HorizontalLayout();
        Label firstnameLabel = new Label(messageService.getMessage("userManagement.firstname.label") + mandatory);
        firstnameLabel.addStyleName("user-management-label");
        firstnameLabel.setId("userManagement.firstname.label");
        firstnameLayout.addComponents(firstnameLabel, firstname);
        userCreateDataBlockRight.addComponent(firstnameLayout);

        HorizontalLayout lastnameLayout = new HorizontalLayout();
        Label lastnameLabel = new Label(messageService.getMessage("userManagement.lastname.label") + mandatory);
        lastnameLabel.addStyleName("user-management-label");
        lastnameLabel.setId("userManagement.lastname.label");
        lastnameLayout.addComponents(lastnameLabel, lastname);
        userCreateDataBlockRight.addComponent(lastnameLayout);

        HorizontalLayout address1Layout = new HorizontalLayout();
        Label addressLabel = new Label(messageService.getMessage("userManagement.address.label"));
        addressLabel.addStyleName("user-management-label");
        addressLabel.setId("userManagement.address.label");
        address1Layout.addComponents(addressLabel, address1);
        userCreateDataBlockRight.addComponent(address1Layout);

        HorizontalLayout address2Layout = new HorizontalLayout();
        Label address2Label = new Label();
        address2Label.addStyleName("user-management-label");
        address2Layout.addComponents(address2Label, address2);
        userCreateDataBlockRight.addComponent(address2Layout);

        HorizontalLayout zipLayout = new HorizontalLayout();
        Label zipLabel = new Label(messageService.getMessage("userManagement.zip.label"));
        zipLabel.addStyleName("user-management-label");
        zipLabel.setId("userManagement.zip.label");
        zipLayout.addComponents(zipLabel, zipCode);
        userCreateDataBlockRight.addComponent(zipLayout);

        HorizontalLayout cityLayout = new HorizontalLayout();
        Label cityLabel = new Label(messageService.getMessage("userManagement.city.label"));
        cityLabel.addStyleName("user-management-label");
        cityLabel.setId("userManagement.city.label");
        cityLayout.addComponents(cityLabel, city);
        userCreateDataBlockRight.addComponent(cityLayout);

        HorizontalLayout countryLayout = new HorizontalLayout();
        Label countryLabel = new Label(messageService.getMessage("userManagement.country.label"));
        countryLabel.addStyleName("user-management-label");
        countryLabel.setId("userManagement.country.label");
        countryLayout.addComponents(countryLabel, country);
        userCreateDataBlockRight.addComponent(countryLayout);

        HorizontalLayout commentLayout = new HorizontalLayout();
        Label commentLabel = new Label(messageService.getMessage("userManagement.comment.label"));
        commentLabel.addStyleName("user-management-label");
        commentLabel.setId("userManagement.comment.label");
        commentLayout.addComponents(commentLabel, comment);
        userCreateDataBlockRight.addComponent(commentLayout);

        country.setItems(countryService.findAll());
        country.setItemCaptionGenerator(Country::getDescription);
    }

    private void initCreateUserFooter(){
        userCreateFooterLayout = new HorizontalLayout();
        userCreateFooterLayout.addStyleName("user-management-footer");
        Button saveUser = new Button("Save User");
        saveUser.addClickListener(e -> saveUser());
        userCreateFooterLayout.addComponent(saveUser);
        userCreateDataWrapper.addComponent(userCreateFooterLayout);
    }

    private void saveUser(){
        binder.readBean(user);
        try {
            binder.validate();
            binder.writeBean(user);
            userService.saveUser(user);

            showAddNotification();
        } catch (ValidationException e) {
            e.printStackTrace();
        } catch (UserException e) {
            loginErrorNotification();
        }

    }

    private void showAddNotification(){
        Notification saveNotification = new Notification("User added!");
        saveNotification.setDelayMsec(3000);
        saveNotification.show(Page.getCurrent());
    }

    private void loginErrorNotification(){
        Notification loginNotification = new Notification("Login name is already in use!", Notification.Type.ERROR_MESSAGE);
        loginNotification.setDelayMsec(3000);
        loginNotification.show(Page.getCurrent());
    }

    private void initBinderAndValidation(){
        binder = new Binder<>(User.class);

        StringLengthValidator notEmptyValidator = new StringLengthValidator(
                messageService.getMessage("userManagement.validation.notEmpty"), 1, 50);

        binder.bindInstanceFields(this);

        binder.forField(employeeNumber).withValidator(notEmptyValidator).bind(User::getEmployeeNo, User::setEmployeeNo);

        binder.forField(email)
                .withValidator(new EmailValidatorAllowEmpty(messageService.getMessage("userManagement.validation.email")))
                .bind(User::getEmail, User::setEmail);

        binder.forField(loginName).withValidator(notEmptyValidator).withValidator(new StringLengthValidator(
                "Login must be between 4 and 20 characters long!",
                4, 20)).bind(User::getUsername, User::setUsername);

        binder.forField(password).withValidator(new StringLengthValidator(
                "Password must contain at least 6 characters!",
                1, 50)).bind(User::getPassword, User::setPassword);

        binder.forField(firstname).withValidator(notEmptyValidator).bind(User::getFirstname, User::setFirstname);
        binder.forField(lastname).withValidator(notEmptyValidator).bind(User::getLastname, User::setLastname);

        user = new User();
        binder.setBean(user);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        init();
    }
}
