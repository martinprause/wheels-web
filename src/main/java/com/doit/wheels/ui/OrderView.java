package com.doit.wheels.ui;

import com.doit.wheels.dao.entities.*;
import com.doit.wheels.dao.entities.basic.AbstractModel;
import com.doit.wheels.services.*;
import com.doit.wheels.ui.nested.WheelRimPositionsLayout;
import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configurable
@SpringComponent
@SpringView(name = OrderView.VIEW_NAME)
public class OrderView extends VerticalLayout implements View {
    static final String VIEW_NAME = "new-order";

    private Layout previousLayout;

    @Autowired
    private MessageByLocaleService messageByLocaleService;

    @Autowired
    private WheelRimPositionService wheelRimPositionService;

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelTypeService modelTypeService;

    @Autowired
    private ValveTypeService valveTypeService;

    private final Binder<Order> SHARED_BINDER = new Binder<>(Order.class);

    private void init(){
        Order bean = new Order();
        bean.setWheelRimPositions(new ArrayList<>());
        SHARED_BINDER.setBean(bean);

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

        positionsButton.addClickListener(clickEvent -> {
            Map<Class, List<? extends AbstractModel>> args = new HashMap<>();
            args.put(Manufacturer.class, manufacturerService.findAll());
            args.put(Model.class, modelService.findAll());
            args.put(ModelType.class, modelTypeService.findAll());
            args.put(ValveType.class, valveTypeService.findAll());
            replaceComponent(new WheelRimPositionsLayout(messageByLocaleService, SHARED_BINDER, args));
        });

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
