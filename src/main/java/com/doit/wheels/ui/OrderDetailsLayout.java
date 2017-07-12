package com.doit.wheels.ui;

import com.doit.wheels.dao.entities.Customer;
import com.doit.wheels.dao.entities.Order;
import com.doit.wheels.dao.entities.User;
import com.doit.wheels.services.CustomerService;
import com.doit.wheels.services.MessageByLocaleService;
import com.doit.wheels.services.OrderService;
import com.doit.wheels.services.UserService;
import com.doit.wheels.utils.UserRoleEnum;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class OrderDetailsLayout extends VerticalLayout{

    private UserService userService;
    private MessageByLocaleService messageService;

    private Binder<Order> binder;

    private OrderService orderService;

    private CustomerService customerService;

    private Order order;

    private final TextField orderNo = new TextField();

    private final DateField created = new DateField();

    private final ComboBox<Customer> customer = new ComboBox<>();

    private final TextField customerNumberOrder = new TextField();

    private final DateField deadlineFinish = new DateField();

    private final DateField deadlineDelivery = new DateField();

    private final Label status = new Label();

    private final ComboBox<User> driver = new ComboBox<>();


    public OrderDetailsLayout(MessageByLocaleService messageByLocaleService,
                              Binder<Order> sharedBinder,
                              OrderService orderService,
                              CustomerService customerService,
                              UserService userService) {
        this.messageService = messageByLocaleService;
        this.orderService = orderService;
        this.customerService = customerService;
        this.userService = userService;
        binder = sharedBinder;
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

        createCustomerButton.addClickListener(e -> UI.getCurrent().getNavigator().navigateTo("create-edit-customer"));
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

    private void initBinderAndValidation(){


        binder.forField(orderNo).bind(Order::getOrderNo, Order::setOrderNo);

        binder.forField(created).withConverter(new LocalDateToDateConverter()).bind(Order::getCreated, Order::setCreated);

        binder.forField(created).withValidator(Objects::nonNull,
                messageService.getMessage("userManagement.validation.notEmpty"))
                .bind(order1 -> formatToLocalDate(order1.getCreated()),
                        (order2, created1) -> order2.setCreated(formatToDate(created1)));

        binder.forField(customer).withValidator(Objects::nonNull, messageService.getMessage("userManagement.validation.notEmpty")).bind(Order::getCustomer, Order::setCustomer);

        binder.forField(customerNumberOrder).bind(Order::getCustomerNumberOrder, Order::setCustomerNumberOrder);

        binder.forField(driver).bind(Order::getDriver, Order::setDriver);

        binder.forField(deadlineFinish).withConverter(new LocalDateToDateConverter()).bind(Order::getDeadlineFinish, Order::setDeadlineFinish);

        binder.forField(deadlineFinish).withValidator(Objects::nonNull,
                messageService.getMessage("userManagement.validation.notEmpty"))
                .bind(order1 -> formatToLocalDate(order1.getDeadlineFinish()),
                        (order2, deadlineFinish1) -> order2.setCreated(formatToDate(deadlineFinish1)));

        binder.forField(deadlineDelivery).withConverter(new LocalDateToDateConverter()).bind(Order::getDeadlineDelivery, Order::setDeadlineDelivery);

        binder.forField(deadlineDelivery).withValidator(Objects::nonNull,
                messageService.getMessage("userManagement.validation.notEmpty"))
                .bind(order1 -> formatToLocalDate(order1.getDeadlineDelivery()),
                        (order2, deadlineDelivery1) -> order2.setCreated(formatToDate(deadlineDelivery1)));

        order = new Order();
        order.setWheelRimPositions(new ArrayList<>());
        binder.setBean(order);
        status.setValue(order.getStatus().name());

    }

    private LocalDate formatToLocalDate(Date date){
        if (date == null){
            return null;
        }
        return date.toInstant().atZone(ZoneId.of("Europe/Berlin")).toLocalDate();
    }

    private Date formatToDate(LocalDate localDate){
        if (localDate == null){
            return null;
        }
        return (Date.from(localDate.atStartOfDay(ZoneId.of("Europe/Berlin")).toInstant()));

    }
}
