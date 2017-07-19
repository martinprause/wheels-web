package com.doit.wheels.ui.nested;

import com.doit.wheels.dao.entities.Customer;
import com.doit.wheels.dao.entities.Order;
import com.doit.wheels.dao.entities.User;
import com.doit.wheels.services.CustomerService;
import com.doit.wheels.services.MessageByLocaleService;
import com.doit.wheels.services.OrderService;
import com.doit.wheels.services.UserService;
import com.doit.wheels.utils.enums.StatusTypeEnum;
import com.doit.wheels.utils.enums.UserRoleEnum;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.data.ValidationException;
import com.vaadin.data.converter.LocalDateTimeToDateConverter;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;

public class OrderDetailsLayout extends VerticalLayout{

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

    public OrderDetailsLayout(MessageByLocaleService messageByLocaleService,
                              Binder<Order> sharedBinder,
                              OrderService orderService,
                              CustomerService customerService,
                              UserService userService,
                              boolean isEditMode) {
        this.messageService = messageByLocaleService;
        this.customerService = customerService;
        this.userService = userService;
        binder = sharedBinder;
        this.isEditMode = isEditMode;
        init();
    }

    public void init() {
        this.setSizeFull();
        this.addStyleName("order-details-layout");
        initUIComponents();
        initBinderAndValidation();
    }

    private void initUIComponents(){
        HorizontalLayout employeeNumberLayout = new HorizontalLayout();
        Label orderNumberLabel = new Label(messageService.getMessage("orderDetails.orderNumber.label"));
        orderNumberLabel.addStyleName("order-details-label");
        orderNumberLabel.setId("orderDetails.orderNumber.label");
        employeeNumberLayout.addComponents(orderNumberLabel, orderNo);
        orderNo.addStyleName("order-details-input-elem");
        this.addComponent(employeeNumberLayout);

        HorizontalLayout orderDateLayout = new HorizontalLayout();
        Label orderDateLabel = new Label(messageService.getMessage("orderDetails.orderDate.label"));
        orderDateLabel.addStyleName("order-details-label");
        orderDateLabel.setId("orderDetails.orderDate.label");
        orderDateLayout.addComponents(orderDateLabel, created);
        created.setDateFormat("EEE, d MMM yyyy HH:mm");
        created.addStyleName("order-details-input-elem");
        created.setLocale(VaadinSession.getCurrent().getLocale());
        created.addValueChangeListener(this::convertOrderNumber);
        this.addComponent(orderDateLayout);

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
            UI.getCurrent().setData(binder.getBean());
            UI.getCurrent().getSession().setAttribute("previousView", "new-order");
            UI.getCurrent().getSession().setAttribute("notSavedOrder", binder.getBean());
            UI.getCurrent().getNavigator().navigateTo("create-edit-customer");
        });

        customerLayout.addComponents(customerLabel,createCustomerButton, customer);
        this.addComponent(customerLayout);

        HorizontalLayout customerOrderNumberLayout = new HorizontalLayout();
        Label customerOrderNumberLabel = new Label(messageService.getMessage("orderDetails.customerOrderNumber.label"));
        customerOrderNumberLabel.setWidth("200px");
        customerOrderNumberLabel.addStyleName("order-details-label");
        customerOrderNumberLabel.setId("orderDetails.customerOrderNumber.label");
        customerOrderNumberLayout.addComponents(customerOrderNumberLabel, customerNumberOrder);
        customerNumberOrder.setWidth("220px");
        this.addComponent(customerOrderNumberLayout);

        HorizontalLayout finishDateLayout = new HorizontalLayout();
        Label finishDateLabel = new Label(messageService.getMessage("orderDetails.finishDate.label"));
        finishDateLabel.addStyleName("order-details-label");
        finishDateLabel.setId("orderDetails.finishDate.label");
        finishDateLayout.addComponents(finishDateLabel, deadlineFinish);
        deadlineFinish.addStyleName("order-details-input-elem");
        deadlineFinish.setDateFormat("EEE, d MMM yyyy HH:mm");
        deadlineFinish.setLocale(VaadinSession.getCurrent().getLocale());
        this.addComponent(finishDateLayout);

        HorizontalLayout deliveryDateLayout = new HorizontalLayout();
        Label deliveryDateLabel = new Label(messageService.getMessage("orderDetails.deliveryDate.label"));
        deliveryDateLabel.addStyleName("order-details-label");
        deliveryDateLabel.setId("orderDetails.deliveryDate.label");
        deliveryDateLayout.addComponents(deliveryDateLabel, deadlineDelivery);
        deadlineDelivery.addStyleName("order-details-input-elem");
        deadlineDelivery.setDateFormat("EEE, d MMM yyyy HH:mm");
        deadlineDelivery.setLocale(VaadinSession.getCurrent().getLocale());
        this.addComponent(deliveryDateLayout);

        HorizontalLayout orderStatusLayout = new HorizontalLayout();
        Label orderStatusLabel = new Label(messageService.getMessage("orderDetails.status.label"));
        orderStatusLabel.addStyleName("order-details-label");
        orderStatusLabel.setId("orderDetails.status.label");
        orderStatusLayout.addComponents(orderStatusLabel, status);
        status.addStyleName("order-details-input-elem");
        this.addComponent(orderStatusLayout);

        HorizontalLayout driverLayout = new HorizontalLayout();
        Label driverLabel = new Label(messageService.getMessage("orderDetails.driver.label"));
        driverLabel.addStyleName("order-details-label");
        driverLabel.setId("orderDetails.driver.label");
        driverLayout.addComponents(driverLabel, driver);
        driver.setEmptySelectionAllowed(false);
        driver.setItems(userService.findAllByRole(UserRoleEnum.DRIVER));
        driver.setItemCaptionGenerator(User::getDriverFullName);
        driver.addStyleName("order-details-input-elem");
        this.addComponent(driverLayout);

    }

    private void convertOrderNumber(HasValue.ValueChangeEvent<LocalDateTime> e) {
        if (e.getValue() != null && orderNo.getValue().equals("")){
            Date date = Date.from(e.getValue().atZone(ZoneId.systemDefault()).toInstant());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMddHHmmss");
            orderNo.setValue(simpleDateFormat.format(date));
        }
    }

    private void initBinderAndValidation(){

        binder.forField(orderNo).bind(Order::getOrderNo, Order::setOrderNo);

        binder.forField(created).withConverter(new LocalDateTimeToDateConverter(ZoneOffset.of("+02:00"))).bind(Order::getCreated, Order::setCreated);

        if (created.getValue() == null){
            created.setValue(LocalDateTime.now());
        }

        binder.forField(created).withValidator(Objects::nonNull,
                messageService.getMessage("userManagement.validation.notEmpty"))
                .bind(order1 -> formatToLocalDateTime(order1.getCreated()),
                        (order2, created1) -> order2.setCreated(formatToDateFromLocalDateTime(created1)));

        binder.forField(customer).withValidator(Objects::nonNull, messageService.getMessage("userManagement.validation.notEmpty")).bind(Order::getCustomer, Order::setCustomer);

        binder.forField(customerNumberOrder).bind(Order::getCustomerNumberOrder, Order::setCustomerNumberOrder);

        binder.forField(driver).bind(Order::getDriver, Order::setDriver);

        binder.forField(deadlineFinish).withConverter(new LocalDateTimeToDateConverter(ZoneOffset.of("+02:00"))).bind(Order::getDeadlineFinish, Order::setDeadlineFinish);

        binder.forField(deadlineFinish).withValidator(Objects::nonNull,
                messageService.getMessage("userManagement.validation.notEmpty"))
                .bind(order1 -> formatToLocalDateTime(order1.getDeadlineFinish()),
                        (order2, deadlineFinish1) -> order2.setCreated(formatToDateFromLocalDateTime(deadlineFinish1)));

        binder.forField(deadlineDelivery).withConverter(new LocalDateTimeToDateConverter(ZoneOffset.of("+02:00"))).bind(Order::getDeadlineDelivery, Order::setDeadlineDelivery);

        binder.forField(deadlineDelivery).withValidator(Objects::nonNull,
                messageService.getMessage("userManagement.validation.notEmpty"))
                .bind(order1 -> formatToLocalDateTime(order1.getDeadlineDelivery()),
                        (order2, deadlineDelivery1) -> order2.setCreated(formatToDateFromLocalDateTime(deadlineDelivery1)));

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

//    private LocalDate formatToLocalDate(Date date){
//        return date == null ? null : date.toInstant().atZone(ZoneId.of("+01:00")).toLocalDate();
//    }
//
//    private Date formatToDate(LocalDate localDate){
//        return localDate == null ? null : (Date.from(localDate.atStartOfDay(ZoneId.of("+01:00")).toInstant()));
//    }

    private LocalDateTime formatToLocalDateTime(Date date){
        return date == null ? null : date.toInstant().atZone(ZoneId.of("Europe/Berlin")).toLocalDateTime();
    }

    private Date formatToDateFromLocalDateTime(LocalDateTime localDateTime){
        return localDateTime == null ? null : (Date.from(localDateTime.atZone(ZoneId.of("Europe/Berlin")).toInstant()));
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
