package com.doit.wheels.ui;

import com.doit.wheels.auth.SecurityUtils;
import com.doit.wheels.services.impl.MessageByLocaleServiceImpl;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.event.selection.SelectionEvent;
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

import java.util.Arrays;
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

    private Button backButton;


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

        ThemeResource resourceHeader = new ThemeResource("img/ico/header-user.png");
        Image userImage = new Image(null, resourceHeader);
        userImage.addStyleName("header-user-image");

        Label headerStatusBar = new Label("Statusbar");
        headerStatusBar.setId("header.statusbar");
        headerStatusBar.setStyleName("header-statusbar-title");

        headerUser = new Label();
        headerUser.setSizeUndefined();
        headerUser.setStyleName("header-user-title");

        CssLayout headerLeft = new CssLayout();
        headerLeft.addStyleName("header-item-left");
        headerLeft.addComponents(userImage, headerStatusBar, headerUser);
        header.addComponent(headerLeft);

        ThemeResource resource = new ThemeResource("img/felgen.png");
        Image image = new Image(null, resource);
        image.setStyleName("header-image");
        image.setWidth("200px");
        image.setHeight("50px");
        header.addComponent(image);

        ComboBox<Label> comboBox = new ComboBox<>();
        Label englishLabel = new Label(messageService.getMessage("localization.english"));
        englishLabel.setId("localization.english");
        englishLabel.setData(Locale.ENGLISH);
        Label germanLabel = new Label(messageService.getMessage("localization.german"));
        germanLabel.setId("localization.german");
        germanLabel.setData(Locale.GERMAN);

        comboBox.setItems(Arrays.asList(englishLabel, germanLabel));
        comboBox.setStyleName("headerButton");
        comboBox.setEmptySelectionAllowed(false);
        comboBox.setSelectedItem(englishLabel);
        comboBox.setTextInputAllowed(false);
        comboBox.setItemCaptionGenerator(Label::getValue);
        comboBox.addSelectionListener(this::changeLocale);

        Button logoutButton = new Button(messageService.getMessage("header.logout"));
        logoutButton.setId("header.logout");
        logoutButton.addClickListener(e -> logout());
        logoutButton.setStyleName("headerButton");
        logoutButton.setVisible(false);
        header.addComponent(logoutButton);

        CssLayout headerRight = new CssLayout();
        headerRight.addStyleName("header-item-right");
        headerRight.addComponents(comboBox, logoutButton);
        header.addComponent(headerRight);

        Panel viewContainer = new Panel();
        viewContainer.setSizeFull();
        layout.addComponent(viewContainer);
        layout.setExpandRatio(viewContainer, 1.0f);

        setContent(layout);
        setErrorHandler(this::handleError);

        Navigator navigator = new Navigator(this, viewContainer);
        navigator.addProvider(viewProvider);
        viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
        navigator.navigateTo(navigator.getState()==null || navigator.getState().equals("") ? "landing" : navigator.getState());

        Button backButton = new Button(messageService.getMessage("header.back"));
        backButton.setId("header.back");
        backButton.addStyleName("headerButton");
        if (navigator.getState().equals("landing")){
            backButton.setVisible(false);
            logoutButton.setVisible(true);
        }
        backButton.addClickListener(e -> back());
        headerRight.addComponent(backButton);

        navigator.addViewChangeListener((ViewChangeListener) viewChangeEvent -> {
            if (!viewChangeEvent.getViewName().equals("landing")){
                logoutButton.setVisible(false);
                backButton.setVisible(true);
            }
            else {
                logoutButton.setVisible(true);
                backButton.setVisible(false);
            }
            return true;
        });
        updateHeaderUser();
    }

    private void back() {
        if (!Boolean.valueOf(getSession().getAttribute("isUserEditMode").toString())){
            this.getNavigator().navigateTo(getSession().getAttribute("previousView").toString());
        }
        else {
            ((UserManagementView) this.getNavigator().getCurrentView()).showManageAccess();
            getSession().setAttribute("isUserEditMode", false);
        }
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

    private void changeLocale(SelectionEvent event){
        if (event.getFirstSelectedItem().isPresent()) {
            Label selectedItem = (Label)event.getFirstSelectedItem().get();
            Locale locale = (Locale) selectedItem.getData();
            messageService.updateLocale(getUI(), locale);
            VaadinSession.getCurrent().setLocale(locale);
        }
    }
}
