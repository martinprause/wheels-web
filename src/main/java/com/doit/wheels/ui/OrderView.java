package com.doit.wheels.ui;

import com.doit.wheels.services.MessageByLocaleService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Layout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@SpringComponent
@SpringView(name = OrderView.VIEW_NAME)
public class OrderView extends VerticalLayout implements View {
    static final String VIEW_NAME = "new-order";

    private Layout previousLayout;

    @Autowired
    private MessageByLocaleService messageByLocaleService;

    private void init(){
        HorizontalLayout menuBar = new HorizontalLayout();
        menuBar.addStyleName("order-menubar");

        Button detailsButton = new Button(messageByLocaleService.getMessage("order.menubar.customerAndOrder.button"));
        detailsButton.setId("order.menubar.customerAndOrder.button");
        detailsButton.addClickListener(e -> replaceComponent(new OrderDetailsLayout(messageByLocaleService)));
        menuBar.addComponent(detailsButton);

        Button positionsButton = new Button(messageByLocaleService.getMessage("order.positions.button"));
        positionsButton.setId("order.positions.button");
        menuBar.addComponent(positionsButton);

        Button guidelines = new Button(messageByLocaleService.getMessage("order.menubar.guidelinesAndPictures.button"));
        guidelines.setId("order.menubar.guidelinesAndPictures.button");
        guidelines.addClickListener(e -> replaceComponent(new GuidelinesLayout(messageByLocaleService)));
        menuBar.addComponent(guidelines);

        Button printButton = new Button(messageByLocaleService.getMessage("order.printAndClose.button"));
        printButton.setId("order.printAndClose.button");
        menuBar.addComponent(printButton);


        this.addComponent(menuBar);

        OrderDetailsLayout orderDetailsLayout = new OrderDetailsLayout(messageByLocaleService);
        previousLayout = orderDetailsLayout;
        this.addComponent(orderDetailsLayout);

    }

    private void replaceComponent(Layout layoutToReplace){
        replaceComponent(previousLayout, layoutToReplace);
        previousLayout = layoutToReplace;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        init();
    }
}
