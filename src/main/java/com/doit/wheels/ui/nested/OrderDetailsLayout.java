package com.doit.wheels.ui.nested;

import com.doit.wheels.dao.entities.*;
import com.doit.wheels.services.CustomerService;
import com.doit.wheels.services.MessageByLocaleService;
import com.doit.wheels.services.UserService;
import com.doit.wheels.utils.enums.StatusTypeEnum;
import com.doit.wheels.utils.enums.UserRoleEnum;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.data.ValidationException;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class OrderDetailsLayout extends HorizontalLayout {

    private UserService userService;
    private MessageByLocaleService messageService;

    private Binder<Order> binder;

    private CustomerService customerService;

    private Order order;

    private final TextField orderNo = new TextField();

    private final DateTimeField created = new DateTimeField();

    private final ComboBox<Customer> customer = new ComboBox<>();

    private final TextField customerNumberOrder = new TextField();

    private final DateTimeField deadlineFinish = new DateTimeField();

    private final DateTimeField deadlineDelivery = new DateTimeField();

    private final Label status = new Label();

    private final ComboBox<User> driver = new ComboBox<>();

    private final boolean isEditMode;
    private VerticalLayout customerInfoLayout;
    private Grid<CustomerContact> contactGrid;
    private VerticalLayout basicCustomerData;

    private Customer sharedCustomer;

    public OrderDetailsLayout(MessageByLocaleService messageByLocaleService,
                              Binder<Order> sharedBinder,
                              CustomerService customerService,
                              UserService userService,
                              boolean isEditMode,
                              Customer sharedCustomer) {
        this.messageService = messageByLocaleService;
        this.customerService = customerService;
        this.userService = userService;
        binder = sharedBinder;
        this.isEditMode = isEditMode;
        this.sharedCustomer = sharedCustomer;
        init();
    }

    public void init() {
        if(sharedCustomer != null)
            binder.getBean().setCustomer(sharedCustomer);
        this.setSizeFull();
        this.addStyleName("order-details-layout");
        this.setSpacing(false);
        initUIComponents();
        initBinderAndValidation();
    }

    private void initUIComponents(){
        VerticalLayout orderLayout = new VerticalLayout();

        HorizontalLayout employeeNumberLayout = new HorizontalLayout();
        Label orderNumberLabel = new Label(messageService.getMessage("orderDetails.orderNumber.label"));
        orderNumberLabel.addStyleName("order-details-label");
        orderNumberLabel.setId("orderDetails.orderNumber.label");
        employeeNumberLayout.addComponents(orderNumberLabel, orderNo);
        orderNo.addStyleName("order-details-input-elem");
        orderLayout.addComponent(employeeNumberLayout);

        HorizontalLayout orderDateLayout = new HorizontalLayout();
        Label orderDateLabel = new Label(messageService.getMessage("orderDetails.orderDate.label"));
        orderDateLabel.addStyleName("order-details-label");
        orderDateLabel.setId("orderDetails.orderDate.label");
        orderDateLayout.addComponents(orderDateLabel, created);
        created.setDateFormat("EEE, d MMM yyyy HH:mm");
        created.addStyleName("order-details-input-elem");
        created.setLocale(VaadinSession.getCurrent().getLocale());
        created.addValueChangeListener(this::convertOrderNumber);
        orderLayout.addComponent(orderDateLayout);

        HorizontalLayout customerLayout = new HorizontalLayout();
        Label customerLabel = new Label(messageService.getMessage("orderDetails.customer.label"));
        customerLabel.addStyleName("order-details-label");
        customerLabel.setId("orderDetails.customer.label");
        customer.setItems(customerService.findAll());
        customer.setItemCaptionGenerator(Customer::getFullName);
        customer.setEmptySelectionAllowed(false);
        customer.setWidth("250px");
        Button createCustomerButton = new Button();
        createCustomerButton.setIcon(new ThemeResource("img/ico/add-16.png"));
        createCustomerButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);

        createCustomerButton.addClickListener(e -> {
            UI.getCurrent().setData(Collections.singletonMap("ORDER", binder.getBean()));
            UI.getCurrent().getSession().setAttribute("previousView", "new-order");
            UI.getCurrent().getSession().setAttribute("notSavedOrder", binder.getBean());
            UI.getCurrent().getNavigator().navigateTo("create-edit-customer");
        });

        customerLayout.addComponents(customerLabel,createCustomerButton, customer);
        orderLayout.addComponent(customerLayout);

        HorizontalLayout customerOrderNumberLayout = new HorizontalLayout();
        Label customerOrderNumberLabel = new Label(messageService.getMessage("orderDetails.customerOrderNumber.label"));
        customerOrderNumberLabel.setWidth("200px");
        customerOrderNumberLabel.addStyleName("order-details-label");
        customerOrderNumberLabel.setId("orderDetails.customerOrderNumber.label");
        customerOrderNumberLayout.addComponents(customerOrderNumberLabel, customerNumberOrder);
        customerNumberOrder.setWidth("220px");
        orderLayout.addComponent(customerOrderNumberLayout);

        HorizontalLayout finishDateLayout = new HorizontalLayout();
        Label finishDateLabel = new Label(messageService.getMessage("orderDetails.finishDate.label"));
        finishDateLabel.addStyleName("order-details-label");
        finishDateLabel.setId("orderDetails.finishDate.label");
        finishDateLayout.addComponents(finishDateLabel, deadlineFinish);
        deadlineFinish.addStyleName("order-details-input-elem");
        deadlineFinish.setDateFormat("EEE, d MMM yyyy HH:mm");
        deadlineFinish.setLocale(VaadinSession.getCurrent().getLocale());
        orderLayout.addComponent(finishDateLayout);

        HorizontalLayout deliveryDateLayout = new HorizontalLayout();
        Label deliveryDateLabel = new Label(messageService.getMessage("orderDetails.deliveryDate.label"));
        deliveryDateLabel.addStyleName("order-details-label");
        deliveryDateLabel.setId("orderDetails.deliveryDate.label");
        deliveryDateLayout.addComponents(deliveryDateLabel, deadlineDelivery);
        deadlineDelivery.addStyleName("order-details-input-elem");
        deadlineDelivery.setDateFormat("EEE, d MMM yyyy HH:mm");
        deadlineDelivery.setLocale(VaadinSession.getCurrent().getLocale());
        orderLayout.addComponent(deliveryDateLayout);

        HorizontalLayout orderStatusLayout = new HorizontalLayout();
        Label orderStatusLabel = new Label(messageService.getMessage("orderDetails.status.label"));
        orderStatusLabel.addStyleName("order-details-label");
        orderStatusLabel.setId("orderDetails.status.label");
        orderStatusLayout.addComponents(orderStatusLabel, status);
        status.addStyleName("order-details-input-elem");
        orderLayout.addComponent(orderStatusLayout);

        HorizontalLayout driverLayout = new HorizontalLayout();
        Label driverLabel = new Label(messageService.getMessage("orderDetails.driver.label"));
        driverLabel.addStyleName("order-details-label");
        driverLabel.setId("orderDetails.driver.label");
        driverLayout.addComponents(driverLabel, driver);
        driver.setEmptySelectionAllowed(false);
        driver.setItems(userService.findAllByRole(UserRoleEnum.DRIVER));
        driver.setItemCaptionGenerator(User::getDriverFullName);
        driver.addStyleName("order-details-input-elem");

        initCustomerInfoLayout();
        orderLayout.addComponent(driverLayout);
        Panel customerInfoPanel = new Panel(customerInfoLayout);
        customerInfoPanel.setWidth("100%");
        customerInfoPanel.addStyleName("customer-details-panel");

        VerticalLayout panelLayout = new VerticalLayout(customerInfoPanel);
        panelLayout.setComponentAlignment(customerInfoPanel, Alignment.MIDDLE_RIGHT);
        panelLayout.setWidth("100%");
        this.addComponents(orderLayout, panelLayout);
        this.setExpandRatio(orderLayout, 3);
        this.setExpandRatio(panelLayout, 4);
        panelLayout.setVisible(false);
        if (customer.getSelectedItem().isPresent()) {
            panelLayout.setVisible(true);
            customerInfoLayout.getParent().addStyleName("customer-details-panel-content");
        }
        customer.addSelectionListener(selectionEvent -> {
            updateCustomerInfoLayoutVisibility();
            panelLayout.setVisible(true);
        });
    }

    private void convertOrderNumber(HasValue.ValueChangeEvent<LocalDateTime> e) {
        if (e.getValue() != null){
            Date date = Date.from(e.getValue().atZone(ZoneId.systemDefault()).toInstant());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMddHHmmss");
            orderNo.setValue(simpleDateFormat.format(date));
        }
    }

    private void initBinderAndValidation(){

        binder.forField(orderNo).bind(Order::getOrderNo, Order::setOrderNo);

        binder.forField(created).withValidator(Objects::nonNull,
                messageService.getMessage("userManagement.validation.notEmpty"))
                .bind(order1 -> formatToLocalDateTime(order1.getCreated()),
                        (order2, created1) -> order2.setCreated(formatToDateFromLocalDateTime(created1)));

        if (created.getValue() == null){
            created.setValue(LocalDateTime.now());
        }

        binder.forField(customer).withValidator(Objects::nonNull, messageService.getMessage("userManagement.validation.notEmpty")).bind(Order::getCustomer, Order::setCustomer);

        binder.forField(customerNumberOrder).bind(Order::getCustomerNumberOrder, Order::setCustomerNumberOrder);

        binder.forField(driver).bind(Order::getDriver, Order::setDriver);

//        binder.forField(deadlineFinish).withValidator(Objects::nonNull,
//                messageService.getMessage("userManagement.validation.notEmpty"))
//                .bind(order1 -> formatToLocalDateTime(order1.getDeadlineFinish()),
//                        (order2, deadlineFinish1) -> order2.setDeadlineFinish(formatToDateFromLocalDateTime(deadlineFinish1)));
//
//        binder.forField(deadlineDelivery).withValidator(Objects::nonNull,
//                messageService.getMessage("userManagement.validation.notEmpty"))
//                .bind(order1 -> formatToLocalDateTime(order1.getDeadlineDelivery()),
//                        (order2, deadlineDelivery1) -> order2.setDeadlineDelivery(formatToDateFromLocalDateTime(deadlineDelivery1)));

        binder.forField(deadlineFinish)
                .bind(order1 -> formatToLocalDateTime(order1.getDeadlineFinish()),
                        (order2, deadlineFinish1) -> order2.setDeadlineFinish(formatToDateFromLocalDateTime(deadlineFinish1)));

        binder.forField(deadlineDelivery)
                .bind(order1 -> formatToLocalDateTime(order1.getDeadlineDelivery()),
                        (order2, deadlineDelivery1) -> order2.setDeadlineDelivery(formatToDateFromLocalDateTime(deadlineDelivery1)));

        if(binder.getBean() == null){
            order = new Order();
            order.setWheelRimPositions(new HashSet<>());
        } else {
            order = binder.getBean();
        }
        binder.setBean(order);
        status.setValue(messageService.getMessage(codeByStatus(order.getStatus())));
        status.setId(codeByStatus(order.getStatus()));
    }

    private void initCustomerInfoLayout() {
        customerInfoLayout = new VerticalLayout();
        Label contactsListLabel = new Label(messageService.getMessage("orderView.furtherCustomerContacts.title"));
        contactsListLabel.setId("orderView.furtherCustomerContacts.title");

        contactGrid = new Grid<>(CustomerContact.class);
        contactGrid.setHeightByRows(3);
        contactGrid.setWidth(600, Unit.PIXELS);

        contactGrid.addStyleName("order-details-contacts-grid");
        contactGrid.setColumns("firstname", "lastname", "email", "phone", "mobile");

        Label firstnameHeader = new Label(messageService.getMessage("customerView.customer.firstName"));
        firstnameHeader.setId("customerView.customer.firstName");
        contactGrid.getDefaultHeaderRow().getCell("firstname").setComponent(firstnameHeader);

        Label lastnameHeader = new Label(messageService.getMessage("customerView.customer.lastName"));
        lastnameHeader.setId("customerView.customer.lastName");
        contactGrid.getDefaultHeaderRow().getCell("lastname").setComponent(lastnameHeader);

        Label emailHeader = new Label(messageService.getMessage("customerView.customer.email"));
        emailHeader.setId("customerView.customer.email");
        contactGrid.getDefaultHeaderRow().getCell("email").setComponent(emailHeader);

        Label phoneHeader = new Label(messageService.getMessage("customerView.customer.phone"));
        phoneHeader.setId("customerView.customer.phone");
        contactGrid.getDefaultHeaderRow().getCell("phone").setComponent(phoneHeader);

        Label mobileHeader = new Label(messageService.getMessage("customerView.customer.mobile"));
        mobileHeader.setId("customerView.customer.mobile");
        contactGrid.getDefaultHeaderRow().getCell("mobile").setComponent(mobileHeader);


        Optional<Customer> selectedCustomerOptional = customer.getSelectedItem();
        if (selectedCustomerOptional.isPresent()) {
            Customer selectedCustomer = customer.getSelectedItem().get();
            basicCustomerData = initCustomerFields(selectedCustomer);
            List<CustomerContact> customerContacts = selectedCustomer.getCustomerContacts();
            if(customerContacts != null) {
                contactGrid.setItems(selectedCustomer.getCustomerContacts());
            }
        } else {
            basicCustomerData = initCustomerFields(new Customer());
        }

        customerInfoLayout.addComponents(basicCustomerData, contactsListLabel, contactGrid);
        customerInfoLayout.setMargin(false);
    }

    private VerticalLayout initCustomerFields(Customer customer) {
        VerticalLayout basicDataLayout = new VerticalLayout();
        basicDataLayout.addStyleName("no-padding-top");
        Label customerNoValue = new Label(customer.getCustomerNo());
        Label firstNameValue = new Label(customer.getFirstname());
        Label lastNameValue = new Label(customer.getLastname());
        Label companyNameValue = new Label(customer.getCompanyName());
        Label address1Value = new Label(customer.getAddress1());
        Label address2Value = new Label(customer.getAddress2());
        Label zipCodeValue = new Label(customer.getZipCode());
        Label cityValue = new Label(customer.getCity());
        Country country = customer.getCountry();
        Label countryValue = new Label(country == null ? "" : country.getDescription());
        Label emailValue = new Label(customer.getEmail());
        Label phoneValue = new Label(customer.getPhone());
        Label mobileValue = new Label(customer.getMobile());

        HorizontalLayout numberLayout = new HorizontalLayout();
        Label numberLabel = new Label(messageService.getMessage("customerView.customer.number"));
        numberLabel.addStyleName("user-management-label");
        numberLabel.setId("customerView.customer.number");
        numberLayout.addComponents(numberLabel, customerNoValue);

        HorizontalLayout companyNameLayout = new HorizontalLayout();
        Label companyNameLabel = new Label(messageService.getMessage("customerView.customer.companyName"));
        companyNameLabel.addStyleName("user-management-label");
        companyNameLabel.setId("customerView.customer.companyName");
        companyNameLayout.addComponents(companyNameLabel, companyNameValue);

        HorizontalLayout firstnamelayout = new HorizontalLayout();
        Label firstnameLabel = new Label(messageService.getMessage("customerView.customer.firstName"));
        firstnameLabel.addStyleName("user-management-label");
        firstnameLabel.setId("customerView.customer.firstName");
        firstnamelayout.addComponents(firstnameLabel, firstNameValue);

        HorizontalLayout lastnameLayout = new HorizontalLayout();
        Label lastnameLabel = new Label(messageService.getMessage("customerView.customer.lastName"));
        lastnameLabel.addStyleName("user-management-label");
        lastnameLabel.setId("customerView.customer.lastName");
        lastnameLayout.addComponents(lastnameLabel, lastNameValue);

        HorizontalLayout addressLayout = new HorizontalLayout();
        Label addressLabel = new Label(messageService.getMessage("customerView.customer.address"));
        addressLabel.addStyleName("user-management-label");
        addressLabel.setId("customerView.customer.address");
        addressLayout.addComponents(addressLabel, address1Value);

        HorizontalLayout adddress2Layout = new HorizontalLayout();
        Label emptyLabel = new Label("");
        emptyLabel.addStyleName("user-management-label");
        adddress2Layout.addComponents(emptyLabel, address2Value);

        HorizontalLayout zipCodeLayout = new HorizontalLayout();
        Label zipLabel = new Label(messageService.getMessage("customerView.customer.zip"));
        zipLabel.addStyleName("user-management-label");
        zipLabel.setId("customerView.customer.zip");
        zipCodeLayout.addComponents(zipLabel, zipCodeValue);

        HorizontalLayout cityLayout = new HorizontalLayout();
        Label cityLabel = new Label(messageService.getMessage("customerView.customer.city"));
        cityLabel.addStyleName("user-management-label");
        cityLabel.setId("customerView.customer.city");
        cityLayout.addComponents(cityLabel, cityValue);

        HorizontalLayout countryLayout = new HorizontalLayout();
        Label countryLabel = new Label(messageService.getMessage("customerView.customer.country"));
        countryLabel.addStyleName("user-management-label");
        countryLabel.setId("customerView.customer.country");
        countryLayout.addComponents(countryLabel, countryValue);

        HorizontalLayout emailLayout = new HorizontalLayout();
        Label emailLabel = new Label(messageService.getMessage("customerView.customer.email"));
        emailLabel.addStyleName("user-management-label");
        emailLabel.setId("customerView.customer.email");
        emailLayout.addComponents(emailLabel, emailValue);

        HorizontalLayout phoneLayout = new HorizontalLayout();
        Label phoneLabel = new Label(messageService.getMessage("customerView.customer.phone"));
        phoneLabel.addStyleName("user-management-label");
        phoneLabel.setId("customerView.customer.phone");
        phoneLayout.addComponents(phoneLabel, phoneValue);

        HorizontalLayout mobileLayout = new HorizontalLayout();
        Label mobileLabel = new Label(messageService.getMessage("customerView.customer.mobile"));
        mobileLabel.addStyleName("user-management-label");
        mobileLabel.setId("customerView.customer.mobile");
        mobileLayout.addComponents(mobileLabel, mobileValue);

        basicDataLayout.addComponents(numberLayout, companyNameLayout, firstnamelayout,
                lastnameLayout, addressLayout, adddress2Layout, zipCodeLayout,
                cityLayout, countryLayout, emailLayout, phoneLayout, mobileLayout);

        return basicDataLayout;
    }

    private void updateCustomerInfoLayoutVisibility() {
        if(customer.getSelectedItem().isPresent()) {
            Customer selectedCustomer = customer.getSelectedItem().get();
            customerInfoLayout.removeComponent(basicCustomerData);
            basicCustomerData = initCustomerFields(selectedCustomer);
            customerInfoLayout.addComponent(basicCustomerData, 0);
            if(selectedCustomer.getCustomerContacts() != null) {
                contactGrid.setItems(selectedCustomer.getCustomerContacts());
            }
            customerInfoLayout.getParent().addStyleName("customer-details-panel-content");
            customerInfoLayout.setVisible(true);
        }
    }

    public boolean validate(){
        try {
            binder.validate();
            binder.writeBean(order);
            return true;
        } catch (ValidationException e) {
            e.printStackTrace();
            return false;
        }
    }

    private LocalDateTime formatToLocalDateTime(Date date){
        return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private Date formatToDateFromLocalDateTime(LocalDateTime localDateTime){
        return localDateTime == null ? null : (Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
    }

    private String codeByStatus(StatusTypeEnum statusEnum) {
        switch (statusEnum) {
            case IN_CREATION:
                return "orderDetails.status.inCreation";
            case CREATED:
                return "orderDetails.status.created";
            case IN_PROCESS:
                return "orderDetails.status.inProcess";
            case PROCESSED:
                return "orderDetails.status.processed";
            case IN_DELIVERY:
                return "orderDetails.status.inDelivery";
            case DELIVERED:
                return "orderDetails.status.delivered";
            default:
                return "orderDetails.status.undefined";
        }
    }
}
