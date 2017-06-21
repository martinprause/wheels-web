package com.doit.wheels.ui;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * UI content when the user is not logged in yet.
 */
//@Configurable
//@SpringComponent
//@SpringView(name = LoginView.NAME)
public class LoginView extends CssLayout{


    private TextField username;
    private PasswordField password;
    private Button login;
    private ComboBox<Locale> locales = new ComboBox<>();

    public LoginView(LoginCallback callback){
        buildUI(callback);
    }

    private void buildUI(LoginCallback callback) {
        addStyleName("login-screen");

        // login form, centered in the available part of the screen
        Component loginForm = buildLoginForm(callback);

        // layout to center login form when there is sufficient screen space
        // - see the theme for how this is made responsive for various screen
        // sizes
        VerticalLayout centeringLayout = new VerticalLayout();
        centeringLayout.setMargin(false);
        centeringLayout.setSpacing(false);
        centeringLayout.setStyleName("centering-layout");
        centeringLayout.addComponent(loginForm);
        centeringLayout.setComponentAlignment(loginForm,
                Alignment.MIDDLE_CENTER);

        addComponent(centeringLayout);
    }

    private Component buildLoginForm(LoginCallback callback) {
        FormLayout loginForm = new FormLayout();

        loginForm.addStyleName("login-form");
        loginForm.setSizeUndefined();
        loginForm.setMargin(false);

        loginForm.addComponent(username = new TextField("Username"));
        username.setWidth(15, Unit.EM);
        loginForm.addComponent(password = new PasswordField("Password"));
        password.setWidth(15, Unit.EM);
        password.setDescription("Write anything");
        CssLayout buttons = new CssLayout();
        buttons.setStyleName("buttons");
        loginForm.addComponent(buttons);

        buttons.addComponent(login = new Button("Login"));
        login.addClickListener((Button.ClickListener) event -> {
            String pword = password.getValue();
            if (!callback.login(username.getValue(), pword)) {
                Notification.show("Login failed");
                username.focus();
            }
        });
        login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        login.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        List<Locale> localeList = new ArrayList<>();
        localeList.add(Locale.ENGLISH);
        localeList.add(Locale.GERMAN);
        locales.setItems(localeList);
//        buttons.addComponent(locales);
        return loginForm;
    }

    @FunctionalInterface
    public interface LoginCallback {

        boolean login(String username, String password);
    }

}
