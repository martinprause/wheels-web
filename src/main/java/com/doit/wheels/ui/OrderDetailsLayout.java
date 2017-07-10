package com.doit.wheels.ui;

import com.doit.wheels.services.MessageByLocaleService;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class OrderDetailsLayout extends VerticalLayout {

    private MessageByLocaleService messageByLocaleService;

    public OrderDetailsLayout(MessageByLocaleService messageByLocaleService) {
        this.messageByLocaleService = messageByLocaleService;
        init();
    }

    private void init() {
        Label orderLabel = new Label(messageByLocaleService.getMessage("order.menubar.customerAndOrder.button"));
        orderLabel.setId("order.menubar.customerAndOrder.button");
        this.addComponent(orderLabel);
    }

}
