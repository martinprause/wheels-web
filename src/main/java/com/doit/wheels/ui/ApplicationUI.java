package com.doit.wheels.ui;

import com.doit.wheels.auth.SecurityUtils;
import com.doit.wheels.services.impl.MessageByLocaleServiceImpl;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.*;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Locale;


@Viewport("user-scalable=no,initial-scale=1.0")
@Theme("mytheme")
@SpringUI
public class ApplicationUI extends UI implements View{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SpringViewProvider viewProvider;

    @Autowired
    private MessageByLocaleServiceImpl messageService;

    private Label headerUser;

    private Button changeLocale;


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Responsive.makeResponsive(this);
        getPage().setTitle("Wheels");
        if (!((getSession().getLocale().equals(Locale.ENGLISH)) || (getSession().getLocale().equals(Locale.GERMAN)))){
            getSession().setLocale(Locale.ENGLISH);
        }
        if (SecurityUtils.isLoggedIn()) {
            showMain();
        } else {
            showLogin();
        }
    }

    private void showLogin() {
        setContent(new LoginView(this::login));
    }

    private void showMain() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeFull();

        CssLayout header = new CssLayout();
        header.setStyleName("header");
        header.setWidth("100%");
        layout.addComponent(header);

        Label headerStatusBar = new Label(messageService.getMessage("header.statusbar"));
        headerStatusBar.setId("header.statusbar");
        headerStatusBar.setStyleName("header-statusbar-title");
        header.addComponent(headerStatusBar);

        headerUser = new Label();
        headerUser.setSizeUndefined();
        header.addComponent(headerUser);
        Button logoutButton = new Button(messageService.getMessage("header.logout"));
        logoutButton.setId("header.logout");
        logoutButton.addClickListener(e -> logout());
        logoutButton.setStyleName("headerButton");
        header.addComponent(logoutButton);
        changeLocale = new Button(messageService.getMessage("localization"));
        changeLocale.setId("localization");
        changeLocale.addClickListener(e -> changeLocale());
        changeLocale.setStyleName("headerButton");
        header.addComponent(changeLocale);
//        header.setComponentAlignment(headerUser, Alignment.MIDDLE_LEFT);

        Panel viewContainer = new Panel();
        viewContainer.setSizeFull();
        layout.addComponent(viewContainer);
        layout.setExpandRatio(viewContainer, 1.0f);

        setContent(layout);
        setErrorHandler(this::handleError);

        Navigator navigator = new Navigator(this, viewContainer);
        navigator.addProvider(viewProvider);
        viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
        navigator.navigateTo("");
        updateHeaderUser();
    }
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

    private boolean login(String username, String password) {
        try {
            Authentication token = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            // Reinitialize the session to protect against session fixation attacks. This does not work
            // with websocket communication.
            VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
            SecurityContextHolder.getContext().setAuthentication(token);
            // Now when the session is reinitialized, we can enable websocket communication. Or we could have just
            // used WEBSOCKET_XHR and skipped this step completely.
            getPushConfiguration().setTransport(Transport.WEBSOCKET);
            getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
            // Show the main UI
            showMain();

            return true;
        } catch (AuthenticationException ex) {
            return false;
        }
    }

    private void handleError(com.vaadin.server.ErrorEvent event) {
        Throwable t = DefaultErrorHandler.findRelevantThrowable(event.getThrowable());
        if (t instanceof AccessDeniedException) {
            Notification.show("You do not have permission to perform this operation",
                    Notification.Type.WARNING_MESSAGE);
        } else {
            DefaultErrorHandler.doDefault(event);
        }
    }

    private void logout() {
        getUI().getPage().reload();
        getSession().close();
    }

    private void updateHeaderUser() {
        headerUser.setValue(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    private void changeLocale(){
        Locale locale = !VaadinSession.getCurrent().getLocale().equals(Locale.ENGLISH) ? Locale.ENGLISH : Locale.GERMAN;
        messageService.updateLocale(getUI(), locale);
        VaadinSession.getCurrent().setLocale(locale);
    }
}
