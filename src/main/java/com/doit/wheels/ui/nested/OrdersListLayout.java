package com.doit.wheels.ui.nested;

import com.doit.wheels.dao.entities.Customer;
import com.doit.wheels.dao.entities.Order;
import com.doit.wheels.dao.entities.PrintJob;
import com.doit.wheels.dao.entities.WheelRimPosition;
import com.doit.wheels.services.MessageByLocaleService;
import com.doit.wheels.services.OrderService;
import com.doit.wheels.services.PrintJobService;
import com.doit.wheels.services.UserService;
import com.doit.wheels.utils.confirmdialog.ConfirmDialog;
import com.doit.wheels.utils.enums.AccessLevelTypeEnum;
import com.doit.wheels.utils.enums.PrintJobStatusEnum;
import com.doit.wheels.utils.enums.StatusTypeEnum;
import com.doit.wheels.utils.exceptions.NoPermissionsException;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.HeaderRow;
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

public class OrdersListLayout extends VerticalLayout {

    private MessageByLocaleService messageByLocaleService;
    private OrderService orderService;
    private PrintJobService printJobService;
    private UserService userService;

    private Order selectedOrder;

    private Grid<Order> orderGrid;

    private Button editOrderButton;
    private Button printOrderButton;
    private Button deleteOrderButton;

    private TextField filterOrderNumber;
    private TextField filterCustomer;
    private TextField filterStatus;
    private TextField filterCustomerZip;
    private TextField filterCustomerOrderNo;
    private TextField filterCreatedDate;
    private TextField filterDeadlineFinish;

    public OrdersListLayout(MessageByLocaleService messageByLocaleService, OrderService orderService, PrintJobService printJobService, UserService userService) {
        this.messageByLocaleService = messageByLocaleService;
        this.orderService = orderService;
        this.printJobService = printJobService;
        this.userService = userService;
        init();
    }

    private void init() {
        this.setWidth("99%");

        orderGrid = new Grid<>(Order.class);
        orderGrid.setId("order-list");
        orderGrid.setWidth("100%");
        orderGrid.setColumns("orderNo");
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

        filterOrderNumber = new TextField();
        filterOrderNumber.addStyleName("filter-field");
        filterOrderNumber.addValueChangeListener(e -> filter());

        filterStatus = new TextField();
        filterStatus.addStyleName("filter-field");
        filterStatus.addValueChangeListener(e -> filter());

        filterCustomer = new TextField();
        filterCustomer.addStyleName("filter-field");
        filterCustomer.addValueChangeListener(e -> filter());

        filterCustomerZip = new TextField();
        filterCustomerZip.addStyleName("filter-field");
        filterCustomerZip.addValueChangeListener(e -> filter());

        filterCustomerOrderNo = new TextField();
        filterCustomerOrderNo.addStyleName("filter-field");
        filterCustomerOrderNo.addValueChangeListener(e -> filter());

        filterCreatedDate = new TextField();
        filterCreatedDate.addStyleName("filter-field");
        filterCreatedDate.addValueChangeListener(e -> filter());

        filterDeadlineFinish = new TextField();
        filterDeadlineFinish.addStyleName("filter-field");
        filterDeadlineFinish.addValueChangeListener(e -> filter());

        HeaderRow filterRow = orderGrid.appendHeaderRow();
        filterRow.setStyleName("filter-row");
        filterRow.getCell("orderNo").setComponent(filterOrderNumber);
        filterRow.getCell("customer").setComponent(filterCustomer);
        filterRow.getCell("status").setComponent(filterStatus);
        filterRow.getCell("customerCity").setComponent(filterCustomerZip);
        filterRow.getCell("customerOrderNo").setComponent(filterCustomerOrderNo);
        filterRow.getCell("created").setComponent(filterCreatedDate);
        filterRow.getCell("deadlineFinish").setComponent(filterDeadlineFinish);

        this.addComponents(orderGrid);
        this.addComponent(initButtonsLayout());
    }

    private void filter() {
        ListDataProvider<Order> dataProvider = (ListDataProvider<Order>) orderGrid.getDataProvider();
        dataProvider.addFilter(Order::getOrderNo, valueFilter -> caseInsensitiveContains(valueFilter, filterOrderNumber.getValue()));
        dataProvider.addFilter(Order::getStatus, valueFilter -> statusFilter(valueFilter, filterStatus.getValue()));
        dataProvider.addFilter((Order order) -> order, valueFilter -> caseInsensitiveContains(formatCustomerCompanyFirstLastName(valueFilter), filterCustomer.getValue()));
        dataProvider.addFilter((Order order) -> order, valueFilter -> caseInsensitiveContains(formatCustomerZipCity(valueFilter), filterCustomerZip.getValue()));
        dataProvider.addFilter(Order::getOrderNo, valueFilter -> caseInsensitiveContains(valueFilter, filterCustomerOrderNo.getValue()));
        dataProvider.addFilter(Order::getCreated, valueFilter -> caseInsensitiveContains(formatOrderDate(valueFilter), filterCreatedDate.getValue()));
        dataProvider.addFilter(Order::getDeadlineFinish, valueFilter -> caseInsensitiveContains(formatOrderDate(valueFilter), filterDeadlineFinish.getValue()));
    }

    private Boolean caseInsensitiveContains(String where, String what) {
        return where.toLowerCase().contains(what.toLowerCase());
    }

    private Boolean statusFilter(StatusTypeEnum status, String what){
        return labelByCode(status).toLowerCase().contains(what.toLowerCase());
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
                        try {
                            orderService.deleteOrder(selectedOrder);
                        } catch (NoPermissionsException e) {
                            Notification.show(messageByLocaleService.getMessage("order.delete.noAccess"),
                                    Notification.Type.ERROR_MESSAGE);
                            e.printStackTrace();
                            return;
                        }
                        Notification.show(messageByLocaleService.getMessage("orderView.order.delete.success"),
                                Notification.Type.HUMANIZED_MESSAGE);
                        orderGrid.setItems(orderService.findAll());
                    }
                });
    }

    private HorizontalLayout initButtonsLayout(){
        editOrderButton = new Button(messageByLocaleService.getMessage("orderView.order.buttons.editOrder"));
        editOrderButton.setId("orderView.order.buttons.editOrder");
        editOrderButton.addStyleName("manage-order-button");
        editOrderButton.addClickListener(clickEvent -> {
            if (userService.checkIfCurrentUserHasPermissions(AccessLevelTypeEnum.EditOrder)) {
                getUI().setData(Collections.singletonMap("ORDER", selectedOrder));
                getUI().getNavigator().navigateTo("new-order");
                UI.getCurrent().getSession().setAttribute("previousView", "customer-orders-list");
            } else {
                Notification.show(messageByLocaleService.getMessage("order.edit.noAccess"),
                        Notification.Type.ERROR_MESSAGE);
            }
        });

        printOrderButton = new Button(messageByLocaleService.getMessage("orderView.order.buttons.print"));
        printOrderButton.setId("orderView.order.buttons.print");
        printOrderButton.addStyleName("manage-order-button");
        printOrderButton.addClickListener(clickEvent -> print());

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
        orderGrid.addColumn(order -> labelByCode(order.getStatus())).setId("status");
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
        orderGrid.addColumn(Order::getCustomerNumberOrder)
                .setId("customerOrderNo");
        defaultGridHeader.getCell("customerOrderNo").setComponent(customerOrderNoHeader);

        Label createdHeader = new Label(messageByLocaleService.getMessage("orderView.order.created"));
        createdHeader.setId("orderView.order.created");
        orderGrid.addColumn(order -> formatOrderDate(order.getCreated())).setId("created");
        defaultGridHeader.getCell("created").setComponent(createdHeader);

        Label deadlineHeader = new Label(messageByLocaleService.getMessage("orderView.order.deadline"));
        deadlineHeader.setId("orderView.order.deadline");
        orderGrid.addColumn(order -> formatOrderDate(order.getDeadlineFinish())).setId("deadlineFinish");
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

    private void print(){
        Notification.show("Print started!", Notification.Type.HUMANIZED_MESSAGE);
        selectedOrder.setQrCode(selectedOrder.getOrderNo() + "-P");
            for (WheelRimPosition wheelRimPosition : selectedOrder.getWheelRimPositions()) {
                wheelRimPosition.setQrCode(selectedOrder.getOrderNo() + "-" + wheelRimPosition.getPositionNo());
            }
            orderService.save(selectedOrder);
        PrintJob printJob = new PrintJob();
        printJob.setOrder(selectedOrder);
        printJob.setUser(userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        printJob.setJobCreated(new Date());
        printJob.setPrintJobStatusEnum(PrintJobStatusEnum.ACTIVE);
        printJobService.save(printJob);
    }

    private String labelByCode(StatusTypeEnum status) {
        switch (status) {
            case IN_CREATION:
                return messageByLocaleService.getMessage("orderDetails.status.inCreation");
            case CREATED:
                return messageByLocaleService.getMessage("orderDetails.status.created");
            case IN_PROCESS:
                return messageByLocaleService.getMessage ("orderDetails.status.inProcess");
            case PROCESSED:
                return messageByLocaleService.getMessage ("orderDetails.status.processed");
            case IN_DELIVERY:
                return messageByLocaleService.getMessage ("orderDetails.status.inDelivery");
            case DELIVERED:
                return messageByLocaleService.getMessage("orderDetails.status.delivered");

            default:
                return messageByLocaleService.getMessage("orderDetails.status.undefined");
        }
    }

    private String formatOrderDate(Date date) {
        DateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return date == null ? "" : newFormat.format(date);
    }

}
