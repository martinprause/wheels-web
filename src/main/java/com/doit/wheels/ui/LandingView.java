package com.doit.wheels.ui;


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@SpringComponent
@SpringView(name = "landing")
public class LandingView extends CssLayout implements View {

    private void init(){
        this.addStyleName("landing-page");
        HorizontalLayout centerMenuBar = new HorizontalLayout();
        centerMenuBar.addStyleName("center-menu-bar");

        VerticalLayout firstButtonGroup = new VerticalLayout();
        firstButtonGroup.setStyleName("button-group");
        Button newOrderButton = new Button("New Order");
        newOrderButton.setIcon(new ThemeResource("img/ico/newOrder.png"));
        newOrderButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        newOrderButton.addStyleName("center-menu-bar-buttons");
        newOrderButton.addClickListener(e -> getUI().getNavigator().navigateTo("home"));

        Button userManagementButton = new Button("User Management");
        userManagementButton.setStyleName("center-menu-bar-buttons");
        userManagementButton.setIcon(new ThemeResource("img/ico/users.png"));
        userManagementButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        firstButtonGroup.addComponents(newOrderButton, userManagementButton);

        centerMenuBar.addComponent(firstButtonGroup);


        VerticalLayout secondButtonGroup = new VerticalLayout();
        secondButtonGroup.addStyleName("button-group");

        Button searchButton = new Button("Search");
        searchButton.addStyleName("center-menu-bar-buttons");
        searchButton.setIcon(new ThemeResource("img/ico/loupe.png"));
        searchButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);

        Button generaLPropertiesButton = new Button("General Properties");
        generaLPropertiesButton.setStyleName("center-menu-bar-buttons");
        generaLPropertiesButton.setIcon(new ThemeResource("img/ico/wrench.png"));
        generaLPropertiesButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);

        secondButtonGroup.addComponents(searchButton, generaLPropertiesButton);
        centerMenuBar.addComponent(secondButtonGroup);

        this.addComponent(centerMenuBar);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        init();
    }
}
