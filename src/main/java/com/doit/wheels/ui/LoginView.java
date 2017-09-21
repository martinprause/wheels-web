package com.doit.wheels.ui;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        Component loginForm = buildLoginForm(callback);
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

        ThemeResource resource = new ThemeResource("img/felgen.png");
        Image image = new Image(null, resource);
        image.addStyleName("login-image");
        image.setWidth("200px");
        image.setHeight("50px");
        loginForm.addComponent(image);

        loginForm.addStyleName("login-form");
        loginForm.setSizeUndefined();
        loginForm.setMargin(false);

        loginForm.addComponent(username = new TextField("Username"));
        username.setWidth(15, Unit.EM);
        loginForm.addComponent(password = new PasswordField("Password"));
        password.setWidth(15, Unit.EM);
        CssLayout buttons = new CssLayout();
        buttons.setStyleName("buttons");

        CheckBox rememberMe = new CheckBox("Remember me");
        loginForm.addComponent(rememberMe);
        loginForm.addComponent(buttons);
        buttons.addComponent(login = new Button("Login"));
        login.setWidth("150px");
        login.addClickListener((Button.ClickListener) event -> {
            String pword = password.getValue();
            if (!callback.login(username.getValue(), pword, rememberMe.getValue())) {
                Notification.show("Login failed");
                username.focus();
            }
        });
        login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        login.addStyleName("theme-buttons");
        List<Locale> localeList = new ArrayList<>();
        localeList.add(Locale.ENGLISH);
        localeList.add(Locale.GERMAN);
        locales.setItems(localeList);


        return loginForm;
    }

    @FunctionalInterface
    public interface LoginCallback {

        boolean login(String username, String password, boolean rememberMe);
    }
}
