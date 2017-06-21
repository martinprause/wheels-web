package com.doit.wheels.ui;

import com.doit.wheels.dao.repositories.UserRepository;
import com.doit.wheels.auth.SecurityUtils;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
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
    AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository service;

    @Autowired
    private SpringViewProvider viewProvider;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Responsive.makeResponsive(this);
        getPage().setTitle("Wheels");
        getSession().setLocale(Locale.ENGLISH);
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
        new Navigator(this, this);
        getNavigator().addProvider(viewProvider);
        getNavigator().navigateTo("");
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
}
