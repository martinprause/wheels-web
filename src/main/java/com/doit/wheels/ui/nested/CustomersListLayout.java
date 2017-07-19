package com.doit.wheels.ui.nested;

import com.doit.wheels.dao.entities.Customer;
import com.doit.wheels.services.CustomerService;
import com.doit.wheels.services.MessageByLocaleService;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.HeaderRow;
import org.vaadin.dialogs.ConfirmDialog;

public class CustomersListLayout extends VerticalLayout {

    private MessageByLocaleService messageByLocaleService;
    private CustomerService customerService;

    private Customer selectedCustomer;

    private Grid<Customer> customerGrid;

    private Button editCustomerButton;
    private Button deleteCustomerButton;

    private TextField filterCustomerNo;
    private TextField filterFirstname;
    private TextField filterLastname;
    private TextField filterCompanyName;
    private TextField filterAddress1;
    private TextField filterZipCode;
    private TextField filterCity;
    private TextField filterEmail;
    private TextField filterPhone;
    private TextField filterMobile;

    public CustomersListLayout(MessageByLocaleService messageByLocaleService, CustomerService customerService) {
        this.messageByLocaleService = messageByLocaleService;
        this.customerService = customerService;

        init();
    }

    private void init() {
        this.setWidth("99%");

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

        filterCustomerNo = new TextField();
        filterCustomerNo.addStyleName("filter-field");
        filterCustomerNo.addValueChangeListener(e -> filter());

        filterFirstname = new TextField();
        filterFirstname.addStyleName("filter-field");
        filterFirstname.addValueChangeListener(e -> filter());

        filterLastname = new TextField();
        filterLastname.addStyleName("filter-field");
        filterLastname.addValueChangeListener(e -> filter());

        filterCompanyName = new TextField();
        filterCompanyName.addStyleName("filter-field");
        filterCompanyName.addValueChangeListener(e -> filter());

        filterAddress1 = new TextField();
        filterAddress1.addStyleName("filter-field");
        filterAddress1.addValueChangeListener(e -> filter());

        filterZipCode = new TextField();
        filterZipCode.addStyleName("filter-field");
        filterZipCode.addValueChangeListener(e -> filter());

        filterCity = new TextField();
        filterCity.addStyleName("filter-field");
        filterCity.addValueChangeListener(e -> filter());

        filterEmail = new TextField();
        filterEmail.addStyleName("filter-field");
        filterEmail.addValueChangeListener(e -> filter());

        filterPhone = new TextField();
        filterPhone.addStyleName("filter-field");
        filterPhone.addValueChangeListener(e -> filter());

        filterMobile = new TextField();
        filterMobile.addStyleName("filter-field");
        filterMobile.addValueChangeListener(e -> filter());


        HeaderRow filterRow = customerGrid.appendHeaderRow();
        filterRow.setStyleName("filter-row");
        filterRow.getCell("customerNo").setComponent(filterCustomerNo);
        filterRow.getCell("firstname").setComponent(filterFirstname);
        filterRow.getCell("lastname").setComponent(filterLastname);
        filterRow.getCell("companyName").setComponent(filterCompanyName);
        filterRow.getCell("address1").setComponent(filterAddress1);
        filterRow.getCell("zipCode").setComponent(filterZipCode);
        filterRow.getCell("city").setComponent(filterCity);
        filterRow.getCell("email").setComponent(filterEmail);
        filterRow.getCell("phone").setComponent(filterPhone);
        filterRow.getCell("mobile").setComponent(filterMobile);

        this.addComponents(customerGrid);

        editCustomerButton = new Button(messageByLocaleService.getMessage("orderView.buttons.editCustomer"));
        editCustomerButton.setId("orderView.buttons.editCustomer");
        editCustomerButton.addStyleName("create-user-button");
        editCustomerButton.addStyleName("edit-customer-button");
        editCustomerButton.addClickListener(clickEvent -> {
            getUI().setData(selectedCustomer);
            getUI().getNavigator().navigateTo("create-edit-customer");
            UI.getCurrent().getSession().setAttribute("previousView", "customer-orders-list");
        });

        deleteCustomerButton = new Button(messageByLocaleService.getMessage("orderView.buttons.deleteCustomer"));
        deleteCustomerButton.setId("orderView.buttons.deleteCustomer");
        deleteCustomerButton.addStyleName("manage-user-access-button");
        deleteCustomerButton.addStyleName("delete-customer-button");
        deleteCustomerButton.addClickListener(clickEvent -> deleteSelectedCustomer());

        enableManagingButtons(false);

        CssLayout bottomMenuLayout = new CssLayout(editCustomerButton, deleteCustomerButton);
        bottomMenuLayout.addStyleName("user-management-menubar");

        this.addComponent(bottomMenuLayout);
    }

    private void filter() {
        ListDataProvider<Customer> dataProvider = (ListDataProvider<Customer>) customerGrid.getDataProvider();
        dataProvider.addFilter(Customer::getCustomerNo, valueFilter -> caseInsensitiveContains(valueFilter, filterCustomerNo.getValue()));
        dataProvider.addFilter(Customer::getFirstname, valueFilter -> caseInsensitiveContains(valueFilter, filterFirstname.getValue()));
        dataProvider.addFilter(Customer::getLastname, valueFilter -> caseInsensitiveContains(valueFilter, filterLastname.getValue()));
        dataProvider.addFilter(Customer::getCompanyName, valueFilter -> caseInsensitiveContains(valueFilter, filterCompanyName.getValue()));
        dataProvider.addFilter(Customer::getAddress1, valueFilter -> caseInsensitiveContains(valueFilter, filterAddress1.getValue()));
        dataProvider.addFilter(Customer::getZipCode, valueFilter -> caseInsensitiveContains(valueFilter, filterZipCode.getValue()));
        dataProvider.addFilter(Customer::getCity, valueFilter -> caseInsensitiveContains(valueFilter, filterCity.getValue()));
        dataProvider.addFilter(Customer::getEmail, valueFilter -> caseInsensitiveContains(valueFilter, filterEmail.getValue()));
        dataProvider.addFilter(Customer::getPhone, valueFilter -> caseInsensitiveContains(valueFilter, filterPhone.getValue()));
        dataProvider.addFilter(Customer::getMobile, valueFilter -> caseInsensitiveContains(valueFilter, filterMobile.getValue()));
    }

    private Boolean caseInsensitiveContains(String where, String what) {
        return where.toLowerCase().contains(what.toLowerCase());
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
}
