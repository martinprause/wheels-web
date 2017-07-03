package com.doit.wheels.ui;

import com.doit.wheels.dao.entities.Country;
import com.doit.wheels.dao.entities.User;
import com.doit.wheels.services.CountryService;
import com.doit.wheels.services.UserService;
import com.doit.wheels.services.impl.MessageByLocaleServiceImpl;
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

@Configurable
@SpringComponent
@SpringView(name = "user-management")
public class UserManagementView extends VerticalLayout implements View {

    private static String mandatory = ":*";

    private CssLayout menubar;
    private HorizontalLayout userData;
    private VerticalLayout userDataBlockLeft;
    private VerticalLayout userDataBlockRight;
    private VerticalLayout userDataWrapper;
    private HorizontalLayout footerLayout;

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

    @Autowired
    MessageByLocaleServiceImpl messageService;

    @Autowired
    UserService userService;

    @Autowired
    CountryService countryService;

    private void init(){
        setSizeFull();

        menubar = new CssLayout();
        menubar.addStyleName("user-management-menubar");
        Button createUserButton = new Button(messageService.getMessage("userManagement.createUser"));
        createUserButton.setId("userManagement.createUser");
        createUserButton.addStyleName("create-user-button");
        createUserButton.setIcon(new ThemeResource("img/ico/new-user.png"));
        createUserButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        createUserButton.addStyleName("clear-button");
        createUserButton.addClickListener(e -> userDataWrapper.setVisible(true));

        Button manageUserAccessButton = new Button(messageService.getMessage("userManagement.manageUserAccess"));
        manageUserAccessButton.setId("userManagement.manageUserAccess");
        manageUserAccessButton.addStyleName("manage-user-access-button");
        manageUserAccessButton.setIcon(new ThemeResource("img/ico/settings.png"));
        manageUserAccessButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        manageUserAccessButton.addStyleName("clear-button");
        manageUserAccessButton.addClickListener(e -> userDataWrapper.setVisible(false));

        menubar.addComponent(createUserButton);
        menubar.addComponent(manageUserAccessButton);
        this.addComponent(menubar);
        userDataWrapper = new VerticalLayout();
        userDataWrapper.setSizeFull();
        userDataWrapper.addStyleName("user-management");

        userData = new HorizontalLayout();
        userData.addStyleName("user-management-data-horizontal");

        userDataBlockLeft = new VerticalLayout();
        userDataBlockLeft.addStyleName("user-management-data-left");
        userData.addComponent(userDataBlockLeft);

        userDataBlockRight = new VerticalLayout();
        userDataBlockRight.addStyleName("user-management-data-right");
        userData.addComponent(userDataBlockRight);
        userDataWrapper.addComponent(userData);
        this.addComponent(userDataWrapper);
        HorizontalLayout footerLayout = new HorizontalLayout();
        footerLayout.addStyleName("user-management-footer");
        Button saveUser = new Button("Save User");
        saveUser.addClickListener(e -> saveUser());
        footerLayout.addComponent(saveUser);
        userDataWrapper.addComponent(footerLayout);
        initLeftPart();
        initRightPart();
        initBinderAndValidation();
    }

    private void initLeftPart(){
        HorizontalLayout employeeNumberLayout = new HorizontalLayout();
        Label employeeNumberLabel = new Label(messageService.getMessage("userManagement.emoloyeeNumber.label") + mandatory);
        employeeNumberLabel.addStyleName("user-management-label");
        employeeNumberLabel.setId("userManagement.emoloyeeNumber.label");
        employeeNumberLayout.addComponents(employeeNumberLabel, employeeNumber);
        userDataBlockLeft.addComponent(employeeNumberLayout);

        HorizontalLayout loginNameLayout = new HorizontalLayout();
        Label loginNameLabel = new Label(messageService.getMessage("userManagement.loginName.label") + mandatory);
        loginNameLabel.addStyleName("user-management-label");
        loginNameLabel.setId("userManagement.loginName.label");
        loginNameLayout.addComponents(loginNameLabel, loginName);
        userDataBlockLeft.addComponent(loginNameLayout);

        HorizontalLayout passwordLayout = new HorizontalLayout();
        Label passwordLabel = new Label(messageService.getMessage("userManagement.password.label") + mandatory);
        passwordLabel.addStyleName("user-management-label");
        passwordLabel.setId("userManagement.password.label");
        passwordLayout.addComponents(passwordLabel, password);
        userDataBlockLeft.addComponent(passwordLayout);

        HorizontalLayout roleLayout = new HorizontalLayout();
        Label roleLabel = new Label(messageService.getMessage("userManagement.role.label") + mandatory);
        roleLabel.addStyleName("user-management-label");
        roleLabel.setId("userManagement.role.label");
        roleLayout.addComponents(roleLabel, role);
        userDataBlockLeft.addComponent(roleLayout);

        HorizontalLayout emailLayout = new HorizontalLayout();
        Label emailLabel = new Label(messageService.getMessage("userManagement.email.label"));
        emailLabel.addStyleName("user-management-label");
        emailLabel.setId("userManagement.email.label");
        emailLayout.addComponents(emailLabel, email);
        userDataBlockLeft.addComponent(emailLayout);

        HorizontalLayout phoneLayout = new HorizontalLayout();
        Label phoneLabel = new Label(messageService.getMessage("userManagement.phone.label"));
        phoneLabel.addStyleName("user-management-label");
        phoneLabel.setId("userManagement.phone.label");
        phoneLayout.addComponents(phoneLabel, phone);
        userDataBlockLeft.addComponent(phoneLayout);

        HorizontalLayout mobileLayout = new HorizontalLayout();
        Label mobileLabel = new Label(messageService.getMessage("userManagement.mobile.label"));
        mobileLabel.addStyleName("user-management-label");
        mobileLabel.setId("userManagement.mobile.label");
        mobileLayout.addComponents(mobileLabel, mobile);
        userDataBlockLeft.addComponent(mobileLayout);

        role.setItems(UserRoleEnum.values());
    }

    private void initRightPart(){
        HorizontalLayout firstnameLayout = new HorizontalLayout();
        Label firstnameLabel = new Label(messageService.getMessage("userManagement.firstname.label") + mandatory);
        firstnameLabel.addStyleName("user-management-label");
        firstnameLabel.setId("userManagement.firstname.label");
        firstnameLayout.addComponents(firstnameLabel, firstname);
        userDataBlockRight.addComponent(firstnameLayout);

        HorizontalLayout lastnameLayout = new HorizontalLayout();
        Label lastnameLabel = new Label(messageService.getMessage("userManagement.lastname.label") + mandatory);
        lastnameLabel.addStyleName("user-management-label");
        lastnameLabel.setId("userManagement.lastname.label");
        lastnameLayout.addComponents(lastnameLabel, lastname);
        userDataBlockRight.addComponent(lastnameLayout);

        HorizontalLayout address1Layout = new HorizontalLayout();
        Label addressLabel = new Label(messageService.getMessage("userManagement.address.label"));
        addressLabel.addStyleName("user-management-label");
        addressLabel.setId("userManagement.address.label");
        address1Layout.addComponents(addressLabel, address1);
        userDataBlockRight.addComponent(address1Layout);

        HorizontalLayout address2Layout = new HorizontalLayout();
        Label address2Label = new Label();
        address2Label.addStyleName("user-management-label");
        address2Layout.addComponents(address2Label, address2);
        userDataBlockRight.addComponent(address2Layout);

        HorizontalLayout zipLayout = new HorizontalLayout();
        Label zipLabel = new Label(messageService.getMessage("userManagement.zip.label"));
        zipLabel.addStyleName("user-management-label");
        zipLabel.setId("userManagement.zip.label");
        zipLayout.addComponents(zipLabel, zipCode);
        userDataBlockRight.addComponent(zipLayout);

        HorizontalLayout cityLayout = new HorizontalLayout();
        Label cityLabel = new Label(messageService.getMessage("userManagement.city.label"));
        cityLabel.addStyleName("user-management-label");
        cityLabel.setId("userManagement.city.label");
        cityLayout.addComponents(cityLabel, city);
        userDataBlockRight.addComponent(cityLayout);

        HorizontalLayout countryLayout = new HorizontalLayout();
        Label countryLabel = new Label(messageService.getMessage("userManagement.country.label"));
        countryLabel.addStyleName("user-management-label");
        countryLabel.setId("userManagement.country.label");
        countryLayout.addComponents(countryLabel, country);
        userDataBlockRight.addComponent(countryLayout);

        HorizontalLayout commentLayout = new HorizontalLayout();
        Label commentLabel = new Label(messageService.getMessage("userManagement.comment.label"));
        commentLabel.addStyleName("user-management-label");
        commentLabel.setId("userManagement.comment.label");
        commentLayout.addComponents(commentLabel, comment);
        userDataBlockRight.addComponent(commentLayout);

        country.setItems(countryService.findAll());
        country.setItemCaptionGenerator(Country::getDescription);
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
        Notification loginNotification = new Notification("Login name is already busy!", Notification.Type.ERROR_MESSAGE);
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
