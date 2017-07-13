package com.doit.wheels.ui;

import com.doit.wheels.dao.entities.*;
import com.doit.wheels.dao.entities.basic.AbstractModel;
import com.doit.wheels.services.*;
import com.doit.wheels.ui.nested.CommentsSubmitLayout;
import com.doit.wheels.ui.nested.WheelRimPositionsLayout;
import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.*;

@Configurable
@SpringComponent
@SpringView(name = OrderView.VIEW_NAME)
public class OrderView extends VerticalLayout implements View {
    static final String VIEW_NAME = "new-order";
    private String CURRENT_MODE;

    private final String CREATE = "Create";
    private final String EDIT = "Edit";

    private Layout previousLayout;

    @Autowired
    private MessageByLocaleService messageByLocaleService;

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelTypeService modelTypeService;

    @Autowired
    private ValveTypeService valveTypeService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    @Autowired
    private GuidelineService guidelineService;

    private final Binder<Order> SHARED_BINDER = new Binder<>(Order.class);

    private void init(){
        Order order;

        if(getUI().getData() != null && getUI().getData().toString().length() > 0) {
            try {
                order = (Order) getUI().getData();
                CURRENT_MODE = EDIT;
            } catch (ClassCastException e) {
                order = new Order();
                order.setWheelRimPositions(new HashSet<>());
                CURRENT_MODE = CREATE;
            }
            getUI().setData(null);
        } else {
            order = new Order();
            order.setWheelRimPositions(new HashSet<>());
            CURRENT_MODE = CREATE;
        }
        SHARED_BINDER.setBean(order);

        HorizontalLayout menuBar = new HorizontalLayout();
        menuBar.addStyleName("order-menubar");

        Button detailsButton = new Button(messageByLocaleService.getMessage("order.menubar.customerAndOrder.button"));
        detailsButton.setId("order.menubar.customerAndOrder.button");
        detailsButton.addClickListener(e -> replaceComponent(new OrderDetailsLayout(messageByLocaleService, SHARED_BINDER, orderService, customerService, userService)));
        detailsButton.addStyleName("clear-button");
        detailsButton.setIcon(new ThemeResource("img/ico/home.png"));
        detailsButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        detailsButton.addStyleName("order-menubar-buttons");
        menuBar.addComponent(detailsButton);

        Button positionsButton = new Button(messageByLocaleService.getMessage("order.positions.button"));
        positionsButton.setId("order.positions.button");
        positionsButton.addStyleName("clear-button");
        positionsButton.setIcon(new ThemeResource("img/ico/star.png"));
        positionsButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        positionsButton.addStyleName("order-menubar-buttons");
        menuBar.addComponent(positionsButton);

        Button guidelines = new Button(messageByLocaleService.getMessage("order.menubar.guidelinesAndPictures.button"));
        guidelines.setId("order.menubar.guidelinesAndPictures.button");
        guidelines.addClickListener(e -> replaceComponent(new GuidelinesLayout(messageByLocaleService, guidelineService, SHARED_BINDER)));
        guidelines.addStyleName("clear-button");
        guidelines.setIcon(new ThemeResource("img/ico/star.png"));
        guidelines.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        guidelines.addStyleName("order-menubar-buttons");
        menuBar.addComponent(guidelines);

        Button printButton = new Button(messageByLocaleService.getMessage("order.printAndClose.button"));
        printButton.setId("order.printAndClose.button");
        printButton.addStyleName("clear-button");
        printButton.setIcon(new ThemeResource("img/ico/star.png"));
        printButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        printButton.addStyleName("order-menubar-buttons");
        printButton.addClickListener(clickEvent -> replaceComponent(new CommentsSubmitLayout(messageByLocaleService, orderService, SHARED_BINDER)));
        menuBar.addComponent(printButton);


        this.addComponent(menuBar);

        OrderDetailsLayout orderDetailsLayout = new OrderDetailsLayout(messageByLocaleService, SHARED_BINDER, orderService, customerService, userService);
        previousLayout = orderDetailsLayout;

        positionsButton.addClickListener(clickEvent -> {
            Map<Class, List<? extends AbstractModel>> args = new HashMap<>();
            args.put(Manufacturer.class, manufacturerService.findAll());
            args.put(Model.class, modelService.findAll());
            args.put(ModelType.class, modelTypeService.findAll());
            args.put(ValveType.class, valveTypeService.findAll());
            replaceComponent(new WheelRimPositionsLayout(messageByLocaleService, SHARED_BINDER, args));
        });
        orderDetailsLayout.setHeight("100%");
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
