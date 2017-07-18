package com.doit.wheels.ui;

import com.doit.wheels.dao.entities.AccessLevel;
import com.doit.wheels.dao.entities.Country;
import com.doit.wheels.dao.entities.User;
import com.doit.wheels.services.AccessLevelService;
import com.doit.wheels.services.CountryService;
import com.doit.wheels.services.UserService;
import com.doit.wheels.services.impl.MessageByLocaleServiceImpl;
import com.doit.wheels.utils.enums.AccessLevelTypeEnum;
import com.doit.wheels.utils.enums.UserRoleEnum;
import com.doit.wheels.utils.exceptions.NoPermissionsException;
import com.doit.wheels.utils.exceptions.UserException;
import com.doit.wheels.utils.validators.EmailValidatorAllowEmpty;
import com.vaadin.annotations.PropertyId;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Configurable
@SpringComponent
@SpringView(name = "user-management")
public class UserManagementView extends VerticalLayout implements View {

    private boolean editMode;

    private boolean hasCreateNewUserPermissions;
    private boolean hasDeleteUserPermissions;

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

    private Button createUserButton;

    private Button manageUserAccessButton;

    private Label editModeLabel;


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

    private HashMap<AccessLevelTypeEnum, CheckBox> accessCheckboxes;

    private CheckBox deleteOrderCheckbox;
    private CheckBox createOrderCheckBox;
    private CheckBox createUserCheckbox;
    private CheckBox deleteUserCheckbox;
    private CheckBox reportsCheckbox;
    private Button saveAccessButton;

    private Label gridCaption;
    private Label accessCaption;

    StringLengthValidator passwordValidator;

    private void init(){
        editMode = false;
//        setSizeFull();
        menubar = new CssLayout();
        menubar.addStyleName("user-management-menubar");

//        editModeLabel = new Label("Edit user");
//        editModeLabel.setVisible(editMode);
//        menubar.addComponent(editModeLabel);

        createUserButton = new Button(messageService.getMessage("userManagement.createUser"));
        createUserButton.setId("userManagement.createUser");
        createUserButton.addStyleName("create-user-button");
        createUserButton.setIcon(new ThemeResource("img/ico/new-user.png"));
        createUserButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        createUserButton.addClickListener(e -> {
            if (hasCreateNewUserPermissions) {
                user = new User();
                binder.setBean(user);
                userAccessWrapper.setVisible(false);
                userCreateDataWrapper.setVisible(true);
                binder.forField(password).withValidator(passwordValidator).bind(User::getPassword, User::setPassword);
                password.setPlaceholder(null);
                makeButtonSelected(createUserButton);
            } else
                Notification.show(messageService.getMessage("user.create.noAccess"),
                        Notification.Type.ERROR_MESSAGE);
        });
        createUserButton.addStyleName("clear-button");
        createUserButton.addStyleName("selected");

        manageUserAccessButton = new Button(messageService.getMessage("userManagement.manageUserAccess"));
        manageUserAccessButton.setId("userManagement.manageUserAccess");
        manageUserAccessButton.addStyleName("manage-user-access-button");
        manageUserAccessButton.setIcon(new ThemeResource("img/ico/settings.png"));
        manageUserAccessButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        manageUserAccessButton.addStyleName("clear-button");
        manageUserAccessButton.addClickListener(e -> {
            makeButtonSelected(manageUserAccessButton);
            userCreateDataWrapper.setVisible(false);
            userAccessWrapper.setVisible(true);
        });

        menubar.addComponent(createUserButton);
        menubar.addComponent(manageUserAccessButton);
        this.addComponent(menubar);

        hasCreateNewUserPermissions = userService.checkIfCurrentUserHasPermissions(AccessLevelTypeEnum.CreateUser);
        hasDeleteUserPermissions = userService.checkIfCurrentUserHasPermissions(AccessLevelTypeEnum.DeleteUser);

        initUserCreateView();
        initUserAccessWrapper();

        if(!hasCreateNewUserPermissions) {
            userCreateDataWrapper.setVisible(false);
            userAccessWrapper.setVisible(true);
            makeButtonSelected(manageUserAccessButton);
        }
    }

    private void initUserCreateView(){
        userCreateDataWrapper = new VerticalLayout();
        userCreateDataWrapper.setSizeFull();
        userCreateDataWrapper.addStyleName("user-management");

        editModeLabel = new Label("Edit user");
        editModeLabel.setVisible(editMode);
        userCreateDataWrapper.addComponent(editModeLabel);
        editModeLabel.addStyleName("edit-mode");

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
        Label employeeNumberLabel = new Label(messageService.getMessage("userManagement.emoloyeeNumber.label"));
        employeeNumberLabel.addStyleName("user-management-label");
        employeeNumberLabel.setId("userManagement.emoloyeeNumber.label");
        employeeNumberLayout.addComponents(employeeNumberLabel, employeeNumber);
        userCreateDataBlockLeft.addComponent(employeeNumberLayout);

        HorizontalLayout loginNameLayout = new HorizontalLayout();
        Label loginNameLabel = new Label(messageService.getMessage("userManagement.loginName.label"));
        loginNameLabel.addStyleName("user-management-label");
        loginNameLabel.setId("userManagement.loginName.label");
        loginNameLayout.addComponents(loginNameLabel, loginName);
        userCreateDataBlockLeft.addComponent(loginNameLayout);

        HorizontalLayout passwordLayout = new HorizontalLayout();
        Label passwordLabel = new Label(messageService.getMessage("userManagement.password.label"));
        passwordLabel.addStyleName("user-management-label");
        passwordLabel.setId("userManagement.password.label");
        passwordLayout.addComponents(passwordLabel, password);
        userCreateDataBlockLeft.addComponent(passwordLayout);

        HorizontalLayout roleLayout = new HorizontalLayout();
        Label roleLabel = new Label(messageService.getMessage("userManagement.role.label"));
        roleLabel.addStyleName("user-management-label");
        roleLabel.setId("userManagement.role.label");
        role.setEmptySelectionAllowed(false);
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
        role.setSelectedItem(UserRoleEnum.ENGINEER);
    }

    private void initUserCreateRightPart(){
        HorizontalLayout firstnameLayout = new HorizontalLayout();
        Label firstnameLabel = new Label(messageService.getMessage("userManagement.firstname.label"));
        firstnameLabel.addStyleName("user-management-label");
        firstnameLabel.setId("userManagement.firstname.label");
        firstnameLayout.addComponents(firstnameLabel, firstname);
        userCreateDataBlockRight.addComponent(firstnameLayout);

        HorizontalLayout lastnameLayout = new HorizontalLayout();
        Label lastnameLabel = new Label(messageService.getMessage("userManagement.lastname.label"));
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
        Button saveUser = new Button(messageService.getMessage("userManagement.saveUser.button"));
        saveUser.setId("userManagement.saveUser.button");
        saveUser.addClickListener(e -> saveUser());
        saveUser.addStyleName("manage-user-access-buttons");
        userCreateFooterLayout.addComponent(saveUser);
        userCreateDataWrapper.addComponent(userCreateFooterLayout);
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
        gridWrapper.setId("grid-wrapper");

        gridCaption = new Label(messageService.getMessage("userManagement.userlist.grid"));
        gridCaption.setId("userManagement.userlist.grid");
        gridCaption.addStyleName("user-access-caption");
        gridWrapper.addComponent(gridCaption);

        Panel panel = new Panel();
        panel.setSizeFull();
        panel.addStyleName("user-access-user-list");

        userGrid = new Grid<>(User.class);
        userGrid.addSelectionListener(e -> selectUserFromGrid());
        updateUsersGrid();

        userGrid.setColumns("username", "firstname", "lastname", "email", "phone", "mobile");

        HeaderRow defaultHeaderRow = userGrid.getDefaultHeaderRow();

        Label usernameLabel = new Label(messageService.getMessage("userManagement.userAccess.grid.username"));
        usernameLabel.setId("userManagement.userAccess.grid.username");
        defaultHeaderRow.getCell("username").setComponent(usernameLabel);

        Label firstnameLabel = new Label(messageService.getMessage("userManagement.userAccess.grid.firstname"));
        firstnameLabel.setId("userManagement.userAccess.grid.firstname");
        defaultHeaderRow.getCell("firstname").setComponent(firstnameLabel);

        Label lastnameLabel = new Label(messageService.getMessage("userManagement.userAccess.grid.lastname"));
        lastnameLabel.setId("userManagement.userAccess.grid.lastname");
        defaultHeaderRow.getCell("lastname").setComponent(lastnameLabel);

        Label emailLabel = new Label(messageService.getMessage("userManagement.userAccess.grid.email"));
        emailLabel.setId("userManagement.userAccess.grid.email");
        defaultHeaderRow.getCell("email").setComponent(emailLabel);

        Label phoneLabel = new Label(messageService.getMessage("userManagement.userAccess.grid.phone"));
        phoneLabel.setId("userManagement.userAccess.grid.phone");
        defaultHeaderRow.getCell("phone").setComponent(phoneLabel);

        Label mobileLabel = new Label(messageService.getMessage("userManagement.userAccess.grid.mobile"));
        mobileLabel.setId("userManagement.userAccess.grid.mobile");
        defaultHeaderRow.getCell("mobile").setComponent(mobileLabel);

        userGrid.setSizeFull();
        panel.setContent(userGrid);
        gridWrapper.addComponent(panel);

        HorizontalLayout userListButtonLayout = new HorizontalLayout();

        editUserButton = new Button();
        editUserButton.setCaption(messageService.getMessage("userManagement.editUser.button"));
        editUserButton.setId("userManagement.editUser.button");
        editUserButton.addClickListener(e -> editUser());
        editUserButton.addStyleName("manage-user-access-buttons");
        userListButtonLayout.addComponent(editUserButton);

        deleteUserButton = new Button();
        deleteUserButton.setCaption(messageService.getMessage("userManagement.deleteUser.button"));
        deleteUserButton.setId("userManagement.deleteUser.button");
        deleteUserButton.addClickListener(e -> deleteUser());
        deleteUserButton.addStyleName("manage-user-access-buttons");
        userListButtonLayout.addComponent(deleteUserButton);
        gridWrapper.addComponent(userListButtonLayout);

        userAccessWrapper.addComponent(gridWrapper);

        VerticalLayout accessesWrapper = new VerticalLayout();
//        accessesWrapper.setWidth("20%");
        accessesWrapper.addStyleName("accesses-wrapper");

        createOrderCheckBox = new CheckBox(messageService.getMessage("userManagement.createOrder.checkbox"));
        createOrderCheckBox.setId("userManagement.createOrder.checkbox");

        deleteOrderCheckbox = new CheckBox(messageService.getMessage("userManagement.deleteOrder.checkbox"));
        deleteOrderCheckbox.setId("userManagement.deleteOrder.checkbox");

        createUserCheckbox = new CheckBox(messageService.getMessage("userManagement.createUser.checkbox"));
        createUserCheckbox.setId("userManagement.createUser.checkbox");

        deleteUserCheckbox = new CheckBox(messageService.getMessage("userManagement.deleteUser.checkbox"));
        deleteUserCheckbox.setId("userManagement.deleteUser.checkbox");

        reportsCheckbox = new CheckBox(messageService.getMessage("userManagement.reports.checkbox"));
        reportsCheckbox.setId("userManagement.reports.checkbox");

        accessCheckboxes = new HashMap<>();
        accessCheckboxes.put(AccessLevelTypeEnum.CreateOrder, createOrderCheckBox);
        accessCheckboxes.put(AccessLevelTypeEnum.DeleteOrder, deleteOrderCheckbox);
        accessCheckboxes.put(AccessLevelTypeEnum.CreateUser, createUserCheckbox);
        accessCheckboxes.put(AccessLevelTypeEnum.DeleteUser, deleteUserCheckbox);
        accessCheckboxes.put(AccessLevelTypeEnum.Reports, reportsCheckbox);

        saveAccessButton = new Button();
        saveAccessButton.setCaption(messageService.getMessage("userManagement.saveAccessRights.button"));
        saveAccessButton.setId("userManagement.saveAccessRights.button");
        saveAccessButton.addClickListener(e -> saveAccesses());
        saveAccessButton.addStyleName("manage-user-access-buttons");
        accessCaption = new Label(messageService.getMessage("userManagement.accessRights.label"));
        accessCaption.setId("userManagement.accessRights.label");
        accessCaption.addStyleName("user-access-caption");
//        userListButtonLayout.addComponent(saveAccessButton);

        accessesWrapper.addComponent(accessCaption);
        accessesWrapper.addComponent(createOrderCheckBox);
        accessesWrapper.addComponent(deleteOrderCheckbox);
        accessesWrapper.addComponent(createUserCheckbox);
        accessesWrapper.addComponent(deleteUserCheckbox);
        accessesWrapper.addComponent(reportsCheckbox);
        accessesWrapper.addComponent(saveAccessButton);
        userAccessWrapper.addComponent(accessesWrapper);

        changeButtonsEnable(false);
    }

    private void saveUser(){
        binder.readBean(user);
        try {
            binder.validate();
            binder.writeBean(user);
            if (editMode){
                userService.updateUser(user);
                editMode = false;
                changeMenubarButtonsVisible();
            }
            else {
                userService.addNewUser(user);
            }
            gotoAccesses();
        } catch (ValidationException e) {
            if (e.getFieldValidationErrors().size() == 1 && editMode && e.getFieldValidationErrors().get(0).getBinding().getField() instanceof PasswordField){
                userService.updateUser(user);
                editMode = false;
                changeMenubarButtonsVisible();
                gotoAccesses();
            }
        } catch (UserException e) {
            loginErrorNotification();
        }
    }

    private void gotoAccesses(){
        User tmpUser = user;
        userGrid.setItems(userService.findAll());
        userGrid.select(tmpUser);
        showManageAccess();
    }

    void showManageAccess(){
        userCreateDataWrapper.setVisible(false);
        userAccessWrapper.setVisible(true);
        editMode = false;
        changeMenubarButtonsVisible();
    }

    private void showAddNotification(){
        Notification saveNotification = new Notification("User added!");
        saveNotification.setDelayMsec(3000);
        saveNotification.show(Page.getCurrent());
    }

    private void loginErrorNotification(){
        Notification loginNotification = new Notification(messageService.getMessage("userManagement.login.notification"), Notification.Type.ERROR_MESSAGE);
        loginNotification.setStyleName("userManagement.login.notification");
        loginNotification.setDelayMsec(3000);
        loginNotification.show(Page.getCurrent());

    }

    private void permissionDeniedNotification(){
        Notification.show(messageService.getMessage("user.delete.noAccess"), Notification.Type.ERROR_MESSAGE);
    }

    private void initBinderAndValidation(){
        binder = new Binder<>(User.class);


        passwordValidator = new StringLengthValidator(
                "Password must contain at least 6 characters!",
                1, 50);

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

        binder.forField(password).withValidator(notEmptyValidator).bind(User::getPassword, User::setPassword);

        binder.forField(role).withValidator(Objects::nonNull, messageService.getMessage("userManagement.validation.notEmpty")).bind(User::getRole, User::setRole);

        binder.forField(firstname).withValidator(notEmptyValidator).bind(User::getFirstname, User::setFirstname);
        binder.forField(lastname).withValidator(notEmptyValidator).bind(User::getLastname, User::setLastname);

        user = new User();
        binder.setBean(user);
    }

    private void updateUsersGrid(){
        userGrid.setItems(userService.findAll());
    }

    private void saveAccesses() {
        if (user != null) {
            Set<AccessLevel> accessLevels = new HashSet<>();
            for (AccessLevelTypeEnum AccessLevelTypeEnum : accessCheckboxes.keySet()) {
                AccessLevel accessLevel = accessLevelService.findAccessLevelByAccessLevel(AccessLevelTypeEnum);
                if (accessCheckboxes.get(AccessLevelTypeEnum).getValue().equals(Boolean.TRUE)){
                    accessLevel.getUsers().add(user);
                    accessLevels.add(accessLevel);
                } else {
                    accessLevel.getUsers().remove(user);
                    accessLevelService.save(accessLevel);
                }
            }
            user.setAccesses(accessLevels);
            userService.save(user);
        }
        updateUsersGrid();
        hasCreateNewUserPermissions = userService.checkIfCurrentUserHasPermissions(AccessLevelTypeEnum.CreateUser);
        hasDeleteUserPermissions = userService.checkIfCurrentUserHasPermissions(AccessLevelTypeEnum.DeleteUser);
    }

    private void deleteUser() {
        if (user != null){
            ConfirmDialog.show(getUI(),
                    messageService.getMessage("user.delete.question"),
                    (ConfirmDialog.Listener) dialog -> {
                        if (dialog.isConfirmed()) {
                            try {
                                userService.removeUserWithAccesses(user);
                                updateUsersGrid();
                            } catch (NoPermissionsException e) {
                                permissionDeniedNotification();
                            }
                        }
                    });
        }
    }

    private void selectUserFromGrid(){
        resetCheckboxes();
        user = userGrid.asSingleSelect().getValue();
        if (user != null){
            if  (user.getAccesses() != null){
                Set<AccessLevel> accessLevels = new HashSet<>(user.getAccesses());
                if (accessLevels.size() != 0){
                    for (AccessLevel accessLevel : accessLevels){
                        accessCheckboxes.get(accessLevel.getAccessLevel()).setValue(true);
                    }
                }
            }
            changeButtonsEnable(true);
        }
        else {
            changeButtonsEnable(false);
        }
    }

    private void changeButtonsEnable(boolean enable){
        editUserButton.setEnabled(enable);
        deleteUserButton.setEnabled(enable);
        saveAccessButton.setEnabled(enable);
    }

    private void changeMenubarButtonsVisible(){
        menubar.setVisible(!editMode);
        editModeLabel.setVisible(editMode);
        changePasswordPresentation();
    }

    private void resetCheckboxes(){
        for (CheckBox checkBox : accessCheckboxes.values()) {
            checkBox.setValue(false);
        }
    }

    private void editUser(){
        binder.setBean(user);
        userAccessWrapper.setVisible(false);
        userCreateDataWrapper.setVisible(true);
        editMode = true;
        changeMenubarButtonsVisible();
        getSession().setAttribute("isUserEditMode", true);
    }

    private void changePasswordPresentation(){
        password.clear();
        password.setPlaceholder(messageService.getMessage("userManagement.password.placeholder"));
        password.setId("userManagement.password.placeholder");
        binder.forField(password).withValidator(Validator.alwaysPass()).bind(User::getPassword, User::setPassword);
    }

    private void makeButtonSelected(Button buttonToSelect) {
        if(buttonToSelect == createUserButton) {
            createUserButton.addStyleName("selected");
            manageUserAccessButton.removeStyleName("selected");
        } else {
            createUserButton.removeStyleName("selected");
            manageUserAccessButton.addStyleName("selected");
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        init();
        getSession().setAttribute("previousView", "landing");
    }


}
