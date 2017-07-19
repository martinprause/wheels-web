package com.doit.wheels.ui;


import com.doit.wheels.services.OrderService;
import com.doit.wheels.services.impl.MessageByLocaleServiceImpl;
import com.doit.wheels.utils.enums.AccessLevelTypeEnum;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@SpringComponent
@SpringView(name = "landing")
public class LandingView extends CssLayout implements View {

    @Autowired
    MessageByLocaleServiceImpl messageService;

    @Autowired
    private OrderService orderService;
    private boolean hasCreateOrderPermissions;
    private boolean hasOrderReviewPermissions;

    private void init(){
        this.addStyleName("landing-page");
        HorizontalLayout centerMenuBar = new HorizontalLayout();
        centerMenuBar.addStyleName("landing-center-menu-bar");

        VerticalLayout firstButtonGroup = new VerticalLayout();
        firstButtonGroup.setStyleName("button-group");
        Button newOrderButton = new Button(messageService.getMessage("landing.newOrder"));
        newOrderButton.setId("landing.newOrder");
        newOrderButton.setIcon(new ThemeResource("img/ico/newOrder.png"));
        newOrderButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        newOrderButton.addStyleName("landing-center-menu-bar-buttons");
        hasCreateOrderPermissions = orderService.checkIfCurrentUserHasPermissions(AccessLevelTypeEnum.CreateOrder);
        newOrderButton.addClickListener(e -> {
            if (hasCreateOrderPermissions) {
                getSession().getSession().setAttribute("previousView", getUI().getNavigator().getState());
                getUI().getNavigator().navigateTo("new-order");
            } else {
                Notification.show(messageService.getMessage("order.create.noAccess"),
                        Notification.Type.ERROR_MESSAGE);
            }
        });

        Button userManagementButton = new Button(messageService.getMessage("landing.userManagement"));
        userManagementButton.setId("landing.userManagement");
        userManagementButton.setStyleName("landing-center-menu-bar-buttons");
        userManagementButton.setIcon(new ThemeResource("img/ico/users.png"));
        userManagementButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        userManagementButton.addClickListener(e -> getUI().getNavigator().navigateTo("user-management"));
        firstButtonGroup.addComponents(newOrderButton, userManagementButton);

        centerMenuBar.addComponent(firstButtonGroup);

        VerticalLayout secondButtonGroup = new VerticalLayout();
        secondButtonGroup.addStyleName("button-group");

        Button searchButton = new Button(messageService.getMessage("landing.search"));
        searchButton.setId("landing.search");
        searchButton.addStyleName("landing-center-menu-bar-buttons");
        searchButton.setIcon(new ThemeResource("img/ico/loupe.png"));
        searchButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        hasOrderReviewPermissions = orderService.checkIfCurrentUserHasPermissions(AccessLevelTypeEnum.Reports);
        searchButton.addClickListener(enter -> {
            if(hasOrderReviewPermissions){
                getUI().getNavigator().navigateTo("customer-orders-list");
            } else {
                Notification.show(messageService.getMessage("reports.noAccess"),
                        Notification.Type.ERROR_MESSAGE);
            }
        });

        Button generaLPropertiesButton = new Button(messageService.getMessage("landing.generalProperties"));
        generaLPropertiesButton.setId("landing.generalProperties");
        generaLPropertiesButton.setStyleName("landing-center-menu-bar-buttons");
        generaLPropertiesButton.setIcon(new ThemeResource("img/ico/wrench.png"));
        generaLPropertiesButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        generaLPropertiesButton.addClickListener(enter -> getUI().getNavigator().navigateTo("general-properties"));

        secondButtonGroup.addComponents(searchButton, generaLPropertiesButton);
        centerMenuBar.addComponent(secondButtonGroup);

        this.addComponent(centerMenuBar);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        init();
    }
}
