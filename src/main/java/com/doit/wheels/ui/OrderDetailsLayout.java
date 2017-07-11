package com.doit.wheels.ui;

import com.doit.wheels.dao.entities.Customer;
import com.doit.wheels.services.MessageByLocaleService;
import com.vaadin.ui.*;

public class OrderDetailsLayout extends VerticalLayout{

    private MessageByLocaleService messageService;

    public OrderDetailsLayout(MessageByLocaleService messageByLocaleService) {
        this.messageService = messageByLocaleService;
        init();
    }

    public void init() {
        this.setSizeFull();
        this.addStyleName("order-details-layout");
        initUIComponents();
    }

    private void initUIComponents(){
        HorizontalLayout employeeNumberLayout = new HorizontalLayout();
        Label orderNumberLabel = new Label(messageService.getMessage("orderDetails.orderNumber.label"));
        orderNumberLabel.addStyleName("order-details-label");
        orderNumberLabel.setId("orderDetails.orderNumber.label");
        TextField orderNumber = new TextField();
        employeeNumberLayout.addComponents(orderNumberLabel, orderNumber);
        this.addComponent(employeeNumberLayout);

        HorizontalLayout orderDateLayout = new HorizontalLayout();
        Label orderDateLabel = new Label("Order date:*");
        orderDateLabel.addStyleName("order-details-label");
        orderDateLabel.setId("orderDetails.orderNumber.label");
        DateField orderDate = new DateField();
        orderDateLayout.addComponents(orderDateLabel, orderDate);
        this.addComponent(orderDateLayout);

        HorizontalLayout customerLayout = new HorizontalLayout();
        Label customerLabel = new Label("Customer:*");
        customerLabel.addStyleName("order-details-label");
        customerLabel.setId("orderDetails.orderNumber.label");
        ComboBox<Customer> customerComboBox = new ComboBox<>();
        customerLayout.addComponents(customerLabel, customerComboBox);
        this.addComponent(customerLayout);

        HorizontalLayout customerButtons = new HorizontalLayout();
        Button assignCustomerButton = new Button("assign");
        assignCustomerButton.addStyleName("customer-button");
        Button createCustomerButton = new Button("create");
        createCustomerButton.addStyleName("customer-button");
        customerButtons.addComponents(createCustomerButton, assignCustomerButton);
        this.addComponent(customerButtons);

        HorizontalLayout customerOrderNumberLayout = new HorizontalLayout();
        Label customerOrderNumberLabel = new Label("Customer order number:");
        customerOrderNumberLabel.addStyleName("order-details-label");
        customerOrderNumberLabel.setId("orderDetails.orderNumber.label");
        TextField customerOrderNumber = new TextField();
        customerOrderNumberLayout.addComponents(customerOrderNumberLabel, customerOrderNumber);
        this.addComponent(customerOrderNumberLayout);

        HorizontalLayout finishDateLayout = new HorizontalLayout();
        Label finishDateLabel = new Label("Finish date:*");
        finishDateLabel.addStyleName("order-details-label");
        finishDateLabel.setId("orderDetails.orderNumber.label");
        DateField finishDate = new DateField();
        finishDateLayout.addComponents(finishDateLabel, finishDate);
        this.addComponent(finishDateLayout);

        HorizontalLayout deliveryDateLayout = new HorizontalLayout();
        Label deliveryDateLabel = new Label("Delivery date:*");
        deliveryDateLabel.addStyleName("order-details-label");
        deliveryDateLabel.setId("orderDetails.orderNumber.label");
        DateField deliveryDate = new DateField();
        deliveryDateLayout.addComponents(deliveryDateLabel, deliveryDate);
        this.addComponent(deliveryDateLayout);

        HorizontalLayout orderStatusLayout = new HorizontalLayout();
        Label orderStatusLabel = new Label("Order Status:");
        orderStatusLabel.addStyleName("order-details-label");
        orderStatusLabel.setId("orderDetails.orderNumber.label");
        TextField orderStatus = new TextField();
        orderStatusLayout.addComponents(orderStatusLabel, orderStatus);
        this.addComponent(orderStatusLayout);

        HorizontalLayout driverLayout = new HorizontalLayout();
        Label driverLabel = new Label("Driver:*");
        driverLabel.addStyleName("order-details-label");
        driverLabel.setId("orderDetails.orderNumber.label");
        ComboBox<Customer> driverComboBox = new ComboBox<>();
        driverLayout.addComponents(driverLabel, driverComboBox);
        this.addComponent(driverLayout);

        HorizontalLayout assingDriverLayout = new HorizontalLayout();
        Button assignDriver = new Button("Assign Driver");
        assingDriverLayout.addComponents(assignDriver);
        this.addComponent(assingDriverLayout);
    }

}
