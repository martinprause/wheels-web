package com.doit.wheels.ui;

import com.doit.wheels.dao.entities.Customer;
import com.doit.wheels.services.CustomerService;
import com.doit.wheels.services.MessageByLocaleService;
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
import org.vaadin.dialogs.ConfirmDialog;

@Configurable
@SpringComponent
@SpringView(name = "customer-orders-list")
@PreserveOnRefresh
public class CustomersOrdersListView extends VerticalLayout implements View {

    private final MessageByLocaleService messageByLocaleService;
    private final CustomerService customerService;

    private Component lastUsedComponent;


    private Grid<Customer> customerGrid;

    private Button editCustomerButton;
    private Button deleteCustomerButton;

    private Customer selectedCustomer;

    @Autowired
    public CustomersOrdersListView(MessageByLocaleService messageByLocaleService, CustomerService customerService) {
        this.messageByLocaleService = messageByLocaleService;
        this.customerService = customerService;
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
        customersNavigationButton.addClickListener(clickEvent -> replaceComponent(generateCustomersList()));

        menubar.addComponents(ordersNavigationButton, customersNavigationButton);

        this.addComponent(menubar);
        lastUsedComponent = generateCustomersList();
        this.addComponent(lastUsedComponent);
    }

    private Layout generateCustomersList() {
        VerticalLayout baseLayout = new VerticalLayout();
        baseLayout.setWidth("99%");
        customerGrid = new Grid<>(Customer.class);
        customerGrid.setWidth("100%");
        customerGrid.setColumns("customerNo", "firstname", "lastname", "companyName",
                "address1", "zipCode", "city", "email", "phone", "mobile");
        initGridHeaders();
        customerGrid.setItems(customerService.findAll());

        customerGrid.addSelectionListener(selectionEvent -> {
            if (selectionEvent.getFirstSelectedItem().isPresent()) {
                selectedCustomer = selectionEvent.getFirstSelectedItem().get();
                enableManagingButtons(true);
            } else {
                selectedCustomer = null;
                enableManagingButtons(false);
            }
        });

        baseLayout.addComponents(customerGrid);

        editCustomerButton = new Button(messageByLocaleService.getMessage("orderView.buttons.editCustomer"));
        editCustomerButton.setId("orderView.buttons.editCustomer");
        editCustomerButton.addStyleName("create-user-button");
        editCustomerButton.addStyleName("edit-customer-button");
        editCustomerButton.addClickListener(clickEvent -> {
            getUI().setData(selectedCustomer);
            getUI().getNavigator().navigateTo("create-edit-customer");
        });

        deleteCustomerButton = new Button(messageByLocaleService.getMessage("orderView.buttons.deleteCustomer"));
        deleteCustomerButton.setId("orderView.buttons.deleteCustomer");
        deleteCustomerButton.addStyleName("manage-user-access-button");
        deleteCustomerButton.addStyleName("delete-customer-button");
        deleteCustomerButton.addClickListener(clickEvent -> deleteSelectedCustomer());

        enableManagingButtons(false);

        CssLayout bottomMenuLayout = new CssLayout(editCustomerButton, deleteCustomerButton);
        bottomMenuLayout.addStyleName("user-management-menubar");

        baseLayout.addComponent(bottomMenuLayout);
        return baseLayout;
    }

    private void enableManagingButtons(boolean flag) {
        editCustomerButton.setEnabled(flag);
        deleteCustomerButton.setEnabled(flag);
    }

    private void deleteSelectedCustomer() {
        ConfirmDialog.show(getUI(),
                messageByLocaleService.getMessage("orderView.deleteCustomer.question"),
                (ConfirmDialog.Listener) dialog -> {
                    if (dialog.isConfirmed()) {
                        Notification.show(messageByLocaleService.getMessage("orderView.deleteCustomer.success"),
                                Notification.Type.HUMANIZED_MESSAGE);
                        customerService.delete(selectedCustomer);
                        customerGrid.setItems(customerService.findAll());
                    }
                });
    }

    private void initGridHeaders() {
        Label customerNoHeader = new Label(messageByLocaleService.getMessage("customerView.customer.number"));
        customerNoHeader.setId("customerView.customer.number");
        customerGrid.getDefaultHeaderRow().getCell("customerNo").setComponent(customerNoHeader);

        Label firstnameHeader = new Label(messageByLocaleService.getMessage("customerView.customer.firstName"));
        firstnameHeader.setId("customerView.customer.firstName");
        customerGrid.getDefaultHeaderRow().getCell("firstname").setComponent(firstnameHeader);

        Label lastnameHeader = new Label(messageByLocaleService.getMessage("customerView.customer.lastName"));
        lastnameHeader.setId("customerView.customer.lastName");
        customerGrid.getDefaultHeaderRow().getCell("lastname").setComponent(lastnameHeader);

        Label companyNameHeader = new Label(messageByLocaleService.getMessage("customerView.customer.companyName"));
        companyNameHeader.setId("customerView.customer.companyName");
        customerGrid.getDefaultHeaderRow().getCell("companyName").setComponent(companyNameHeader);

        Label address1Header = new Label(messageByLocaleService.getMessage("customerView.customer.address"));
        address1Header.setId("customerView.customer.address");
        customerGrid.getDefaultHeaderRow().getCell("address1").setComponent(address1Header);

        Label zipCodeHeader = new Label(messageByLocaleService.getMessage("customerView.customer.zip"));
        zipCodeHeader.setId("customerView.customer.zip");
        customerGrid.getDefaultHeaderRow().getCell("zipCode").setComponent(zipCodeHeader);

        Label cityHeader = new Label(messageByLocaleService.getMessage("customerView.customer.city"));
        cityHeader.setId("customerView.customer.city");
        customerGrid.getDefaultHeaderRow().getCell("city").setComponent(cityHeader);

        Label emailHeader = new Label(messageByLocaleService.getMessage("customerView.customer.email"));
        emailHeader.setId("customerView.customer.email");
        customerGrid.getDefaultHeaderRow().getCell("email").setComponent(emailHeader);

        Label phoneHeader = new Label(messageByLocaleService.getMessage("customerView.customer.phone"));
        phoneHeader.setId("customerView.customer.phone");
        customerGrid.getDefaultHeaderRow().getCell("phone").setComponent(phoneHeader);

        Label mobileHeader = new Label(messageByLocaleService.getMessage("customerView.customer.mobile"));
        mobileHeader.setId("customerView.customer.mobile");
        customerGrid.getDefaultHeaderRow().getCell("mobile").setComponent(mobileHeader);
    }

    private void replaceComponent(Component newComponent) {
        replaceComponent(lastUsedComponent, newComponent);
        lastUsedComponent = newComponent;
    }
}
