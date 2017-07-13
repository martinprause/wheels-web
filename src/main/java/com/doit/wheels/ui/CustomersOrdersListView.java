package com.doit.wheels.ui;

import com.doit.wheels.dao.entities.Customer;
import com.doit.wheels.services.CustomerService;
import com.doit.wheels.services.MessageByLocaleService;
import com.doit.wheels.services.OrderService;
import com.doit.wheels.ui.nested.CustomersListLayout;
import com.doit.wheels.ui.nested.OrdersListLayout;
import com.vaadin.annotations.PreserveOnRefresh;
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
@SpringView(name = "customer-orders-list")
@PreserveOnRefresh
public class CustomersOrdersListView extends VerticalLayout implements View {

    private final MessageByLocaleService messageByLocaleService;
    private final CustomerService customerService;
    private final OrderService orderService;

    private Component lastUsedComponent;


    private Grid<Customer> customerGrid;

    private Button editCustomerButton;
    private Button deleteCustomerButton;

    private Customer selectedCustomer;

    @Autowired
    public CustomersOrdersListView(MessageByLocaleService messageByLocaleService, CustomerService customerService,
                                   OrderService orderService) {
        this.messageByLocaleService = messageByLocaleService;
        this.customerService = customerService;
        this.orderService = orderService;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        init();
    }

    private void init() {
        CssLayout menubar = new CssLayout();
        menubar.addStyleName("user-management-menubar");

        Button ordersNavigationButton = new Button(messageByLocaleService.getMessage("orderView.navigation.order"));
        ordersNavigationButton.setId("orderView.navigation.order");
        ordersNavigationButton.addStyleName("create-user-button");
        ordersNavigationButton.setIcon(new ThemeResource("img/ico/orders.png"));
        ordersNavigationButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        ordersNavigationButton.addStyleName("clear-button");
        ordersNavigationButton.addStyleName("order-list-button");

        Button customersNavigationButton = new Button(messageByLocaleService.getMessage("orderView.navigation.customer"));
        customersNavigationButton.setId("orderView.navigation.customer");
        customersNavigationButton.addStyleName("manage-user-access-button");
        customersNavigationButton.setIcon(new ThemeResource("img/ico/customers.png"));
        customersNavigationButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        customersNavigationButton.addStyleName("clear-button");
        customersNavigationButton.addStyleName("customer-list-button");
        customersNavigationButton.addClickListener(clickEvent -> replaceComponent(new CustomersListLayout(messageByLocaleService, customerService)));

        menubar.addComponents(ordersNavigationButton, customersNavigationButton);

        this.addComponent(menubar);
        lastUsedComponent = new OrdersListLayout(messageByLocaleService, orderService);
        this.addComponent(lastUsedComponent);
    }


    private void replaceComponent(Component newComponent) {
        replaceComponent(lastUsedComponent, newComponent);
        lastUsedComponent = newComponent;
    }
}
