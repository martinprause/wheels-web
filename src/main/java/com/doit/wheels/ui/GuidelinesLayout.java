package com.doit.wheels.ui;

import com.doit.wheels.services.MessageByLocaleService;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class GuidelinesLayout extends VerticalLayout{

    private MessageByLocaleService messageByLocaleService;

    public GuidelinesLayout(MessageByLocaleService messageByLocaleService) {
        this.messageByLocaleService = messageByLocaleService;
        init();
    }

    private void init() {
        Label orderLabel = new Label(messageByLocaleService.getMessage("order.menubar.guidelinesAndPictures.button"));
        orderLabel.setId("order.menubar.guidelinesAndPictures.button");
        this.addComponent(orderLabel);
    }

}
