package com.doit.wheels.ui;

import com.doit.wheels.dao.entities.*;
import com.doit.wheels.dao.entities.basic.AbstractModel;
import com.doit.wheels.services.*;
import com.doit.wheels.ui.nested.CommentsSubmitLayout;
import com.doit.wheels.ui.nested.GuidelinesLayout;
import com.doit.wheels.ui.nested.OrderDetailsLayout;
import com.doit.wheels.ui.nested.WheelRimPositionsLayout;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
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
import org.vaadin.dialogs.ConfirmDialog;

import java.util.*;

@Configurable
@SpringComponent
@SpringView(name = OrderView.VIEW_NAME)
public class OrderView extends VerticalLayout implements View {
    static final String VIEW_NAME = "new-order";
    public String CURRENT_MODE;

    private final String CREATE = "Create";
    private final String EDIT = "Edit";

    private Layout previousLayout;

    private final MessageByLocaleService messageByLocaleService;

    private final ManufacturerService manufacturerService;

    private final ModelService modelService;

    private final ModelTypeService modelTypeService;

    private final ValveTypeService valveTypeService;

    private final OrderService orderService;

    private final CustomerService customerService;

    private final UserService userService;

    private final GuidelineService guidelineService;

    private final PrintJobService printJobService;

    private final Binder<Order> SHARED_BINDER = new Binder<>(Order.class);
    private Button detailsButton;
    private Button positionsButton;
    private Button guidelines;
    private Button printButton;

    private Customer sharedCustomer;

    private Order order;

    @Autowired
    public OrderView(MessageByLocaleService messageByLocaleService, ManufacturerService manufacturerService,
                     ModelService modelService, ModelTypeService modelTypeService, ValveTypeService valveTypeService,
                     OrderService orderService, CustomerService customerService, UserService userService,
                     GuidelineService guidelineService, PrintJobService printJobService) {
        this.messageByLocaleService = messageByLocaleService;
        this.manufacturerService = manufacturerService;
        this.modelService = modelService;
        this.modelTypeService = modelTypeService;
        this.valveTypeService = valveTypeService;
        this.orderService = orderService;
        this.customerService = customerService;
        this.userService = userService;
        this.guidelineService = guidelineService;
        this.printJobService = printJobService;
    }

    private void init(){
        Object data = getUI().getData();

        if(data != null && data.toString().length() > 0) {
            Order sharedOrder = (Order) ((Map) data).get("ORDER");
            if(sharedOrder != null){
                order = sharedOrder;
                CURRENT_MODE = sharedOrder.getId() == null ? CREATE : EDIT;
            } else {
                order = new Order();
                order.setWheelRimPositions(new HashSet<>());
                CURRENT_MODE = CREATE;
            }
            sharedCustomer = (Customer) ((Map) data).get("CUSTOMER");
            getUI().setData(null);
        } else {
            order = new Order();
            order.setWheelRimPositions(new HashSet<>());
            CURRENT_MODE = CREATE;
        }
        SHARED_BINDER.setBean(order);

        HorizontalLayout menuBar = new HorizontalLayout();
        menuBar.addStyleName("order-menubar");

        detailsButton = new Button(messageByLocaleService.getMessage("order.menubar.customerAndOrder.button"));
        detailsButton.setId("order.menubar.customerAndOrder.button");
        detailsButton.addClickListener(e -> {
            makeButtonSelected(detailsButton);
            replaceComponent(new OrderDetailsLayout(messageByLocaleService, SHARED_BINDER, customerService, userService,CURRENT_MODE.equals(EDIT), sharedCustomer));
        });
        detailsButton.addStyleName("clear-button");
        detailsButton.setIcon(new ThemeResource("img/ico/home.png"));
        detailsButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        detailsButton.addStyleName("order-menubar-buttons");
        detailsButton.addStyleName("selected");
        menuBar.addComponent(detailsButton);

        positionsButton = new Button(messageByLocaleService.getMessage("order.positions.button"));
        positionsButton.setId("order.positions.button");
        positionsButton.addClickListener(clickEvent -> {
            makeButtonSelected(positionsButton);
            Map<Class, List<? extends AbstractModel>> args = new HashMap<>();
            args.put(Manufacturer.class, manufacturerService.findAll());
            args.put(Model.class, modelService.findAll());
            args.put(ModelType.class, modelTypeService.findAll());
            args.put(ValveType.class, valveTypeService.findAll());
            replaceComponent(new WheelRimPositionsLayout(messageByLocaleService, SHARED_BINDER, args));
        });
        positionsButton.addStyleName("clear-button");
        positionsButton.setIcon(new ThemeResource("img/ico/star.png"));
        positionsButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        positionsButton.addStyleName("order-menubar-buttons");
        menuBar.addComponent(positionsButton);

        guidelines = new Button(messageByLocaleService.getMessage("order.menubar.guidelinesAndPictures.button"));
        guidelines.setId("order.menubar.guidelinesAndPictures.button");
        guidelines.addClickListener(e -> {
            makeButtonSelected(guidelines);
            replaceComponent(new GuidelinesLayout(messageByLocaleService, guidelineService, SHARED_BINDER, orderService));
        });
        guidelines.addStyleName("clear-button");
        guidelines.setIcon(new ThemeResource("img/ico/star.png"));
        guidelines.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        guidelines.addStyleName("order-menubar-buttons");
        menuBar.addComponent(guidelines);

        printButton = new Button(messageByLocaleService.getMessage("order.commentsAndSubmit.button"));
        printButton.setId("order.commentsAndSubmit.button");
        printButton.addStyleName("clear-button");
        printButton.setIcon(new ThemeResource("img/ico/star.png"));
        printButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        printButton.addStyleName("order-menubar-buttons");
        printButton.addClickListener(clickEvent -> {
            makeButtonSelected(printButton);
            replaceComponent(new CommentsSubmitLayout(messageByLocaleService, orderService, userService, printJobService, SHARED_BINDER, CURRENT_MODE.equals(EDIT)));
        });
        menuBar.addComponent(printButton);


        this.addComponent(menuBar);

        OrderDetailsLayout orderDetailsLayout = new OrderDetailsLayout(messageByLocaleService, SHARED_BINDER, customerService, userService, CURRENT_MODE.equals(EDIT), sharedCustomer );
        previousLayout = orderDetailsLayout;

        orderDetailsLayout.setHeight("100%");
        this.addComponent(orderDetailsLayout);

    }

    private void replaceComponent(Layout layoutToReplace){
        boolean validated = true;
        if (previousLayout instanceof OrderDetailsLayout){
            validated = ((OrderDetailsLayout) previousLayout).validate();
        }
        if (validated){
            replaceComponent(previousLayout, layoutToReplace);
            previousLayout = layoutToReplace;
        } else {
            makeButtonSelected(detailsButton);
        }
    }

    private void makeButtonSelected(Button buttonToSelect){
        List<Button> buttons = Arrays.asList(detailsButton, positionsButton, printButton, guidelines);
        buttons.forEach(button -> button.removeStyleName("selected"));
        buttonToSelect.addStyleName("selected");
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        init();
        String previousView = CURRENT_MODE.equals(EDIT) ? "customer-orders-list" : "landing";
        getSession().setAttribute("previousView", previousView);
        if (viewChangeEvent.getOldView() instanceof CreateEditCustomerView){
            SHARED_BINDER.setBean((Order) getSession().getAttribute("notSavedOrder"));
        }

    }

    void saveChangesPopup() {
        ConfirmDialog.show(getUI(),
                messageByLocaleService.getMessage("save.notification.title"),
                messageByLocaleService.getMessage("save.notification.body"),
                messageByLocaleService.getMessage("save.notification.okCaption"),
                messageByLocaleService.getMessage("save.notification.cancelCaption"),
                messageByLocaleService.getMessage("save.notification.notOkCaption"),
                (ConfirmDialog.Listener) dialog -> {
                    if (dialog.isConfirmed()) {
                        SHARED_BINDER.validate();
                        try {
                            SHARED_BINDER.writeBean(order);
                            orderService.save(SHARED_BINDER.getBean());
                            getUI().getNavigator().navigateTo(getSession().getAttribute("previousView").toString());
                        } catch (ValidationException e) {
                            e.printStackTrace();
                        }
                    }
                    if (dialog.isNotConfirmed()) {
                        getUI().getNavigator().navigateTo(getSession().getAttribute("previousView").toString());
                    }
                });

    }
}
