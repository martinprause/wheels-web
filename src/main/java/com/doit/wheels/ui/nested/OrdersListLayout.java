package com.doit.wheels.ui.nested;

import com.doit.wheels.dao.entities.Customer;
import com.doit.wheels.dao.entities.Order;
import com.doit.wheels.services.MessageByLocaleService;
import com.doit.wheels.services.OrderService;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.HeaderRow;
import org.vaadin.dialogs.ConfirmDialog;

public class OrdersListLayout extends VerticalLayout {

    private MessageByLocaleService messageByLocaleService;
    private OrderService orderService;

    private Order selectedOrder;

    private Grid<Order> orderGrid;

    private Button editOrderButton;
    private Button printOrderButton;
    private Button deleteOrderButton;

    public OrdersListLayout(MessageByLocaleService messageByLocaleService, OrderService orderService) {
        this.messageByLocaleService = messageByLocaleService;
        this.orderService = orderService;

        init();
    }

    private void init() {
        this.setWidth("99%");

        orderGrid = new Grid<>(Order.class);
        orderGrid.setWidth("100%");
        orderGrid.setColumns("orderNo", "status", "created", "deadlineFinish");
        initGridHeaders();
        orderGrid.setItems(orderService.findAll());

        orderGrid.addSelectionListener(selectionEvent -> {
            boolean present = selectionEvent.getFirstSelectedItem().isPresent();
            if (present)
                selectedOrder = selectionEvent.getFirstSelectedItem().get();
            else
                selectedOrder = null;
            enableManagingButtons(present);
        });

        this.addComponents(orderGrid);
        this.addComponent(initButtonsLayout());
    }

    private void enableManagingButtons(boolean flag) {
        editOrderButton.setEnabled(flag);
        printOrderButton.setEnabled(flag);
        deleteOrderButton.setEnabled(flag);
    }

    private void deleteSelectedOrder() {
        ConfirmDialog.show(getUI(),
                messageByLocaleService.getMessage("orderView.order.delete.question"),
                (ConfirmDialog.Listener) dialog -> {
                    if (dialog.isConfirmed()) {
                        Notification.show(messageByLocaleService.getMessage("orderView.order.delete.success"),
                                Notification.Type.HUMANIZED_MESSAGE);
                        orderService.delete(selectedOrder);
                        orderGrid.setItems(orderService.findAll());
                    }
                });
    }

    private HorizontalLayout initButtonsLayout(){
        editOrderButton = new Button(messageByLocaleService.getMessage("orderView.order.buttons.editOrder"));
        editOrderButton.setId("orderView.order.buttons.editOrder");
        editOrderButton.addStyleName("manage-order-button");
        editOrderButton.addClickListener(clickEvent -> {
            getUI().setData(selectedOrder);
            getUI().getNavigator().navigateTo("new-order");
            UI.getCurrent().getSession().setAttribute("previousView", "customer-orders-list");
        });

        printOrderButton = new Button(messageByLocaleService.getMessage("orderView.order.buttons.print"));
        printOrderButton.setId("orderView.order.buttons.print");
        printOrderButton.addStyleName("manage-order-button");
        printOrderButton.addClickListener(clickEvent -> Notification.show("HERE WILL BE PRINT!", Notification.Type.HUMANIZED_MESSAGE));

        deleteOrderButton = new Button(messageByLocaleService.getMessage("orderView.order.buttons.deleteOrder"));
        deleteOrderButton.setId("orderView.order.buttons.deleteOrder");
        deleteOrderButton.addStyleName("manage-order-button");
        deleteOrderButton.addClickListener(clickEvent -> deleteSelectedOrder());

        enableManagingButtons(false);

        HorizontalLayout bottomMenuLayout = new HorizontalLayout();
        bottomMenuLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        bottomMenuLayout.addComponents(editOrderButton, printOrderButton, deleteOrderButton);
        bottomMenuLayout.setWidth("99%");
        return bottomMenuLayout;
    }

    private void initGridHeaders() {
        HeaderRow defaultGridHeader = orderGrid.getDefaultHeaderRow();

        Label orderNoHeader = new Label(messageByLocaleService.getMessage("orderView.order.orderNo"));
        orderNoHeader.setId("orderView.order.orderNo");
        defaultGridHeader.getCell("orderNo").setComponent(orderNoHeader);

        Label statusHeader = new Label(messageByLocaleService.getMessage("orderView.order.status"));
        statusHeader.setId("orderView.order.status");
        defaultGridHeader.getCell("status").setComponent(statusHeader);

        Label customerDataHeader = new Label(messageByLocaleService.getMessage("orderView.order.customer"));
        customerDataHeader.setId("orderView.order.customer");
        orderGrid.addColumn(this::formatCustomerCompanyFirstLastName).setId("customer");
        defaultGridHeader.getCell("customer").setComponent(customerDataHeader);

        Label companyNameHeader = new Label(messageByLocaleService.getMessage("orderView.order.customerCity"));
        companyNameHeader.setId("orderView.order.customerCity");
        orderGrid.addColumn(this::formatCustomerZipCity).setId("customerCity");
        defaultGridHeader.getCell("customerCity").setComponent(companyNameHeader);

        Label customerOrderNoHeader = new Label(messageByLocaleService.getMessage("orderView.order.customerOrderNo"));
        customerOrderNoHeader.setId("orderView.order.customerOrderNo");
        orderGrid.addColumn(order -> order.getCustomer() == null ? "" : order.getCustomer().getCustomerNo())
                .setId("customerOrderNo");
        defaultGridHeader.getCell("customerOrderNo").setComponent(customerOrderNoHeader);

        Label createdHeader = new Label(messageByLocaleService.getMessage("orderView.order.created"));
        createdHeader.setId("orderView.order.created");
        defaultGridHeader.getCell("created").setComponent(createdHeader);

        Label deadlineHeader = new Label(messageByLocaleService.getMessage("orderView.order.deadline"));
        deadlineHeader.setId("orderView.order.deadline");
        defaultGridHeader.getCell("deadlineFinish").setComponent(deadlineHeader);

        orderGrid.setColumnOrder("orderNo", "status", "customer", "customerCity",
                "customerOrderNo", "created", "deadlineFinish");
    }

    private String formatCustomerCompanyFirstLastName(Order order) {
        Customer customer = order.getCustomer();
        if(customer == null) return null;
        String company = customer.getCompanyName() == null ? "" : customer.getCompanyName() + " ";
        String firstName = customer.getFirstname() == null ? "" : customer.getFirstname() + " ";
        String lastName = customer.getLastname() == null ? "" : customer.getLastname() + " ";
        return company + firstName + lastName;
    }

    private String formatCustomerZipCity(Order order) {
        Customer customer = order.getCustomer();
        if(customer == null) return null;
        String zipCode = customer.getZipCode() == null ? "" : customer.getZipCode() + "/";
        String city = customer.getCity() == null ? "" : customer.getCity();
        return zipCode + city;
    }
}
