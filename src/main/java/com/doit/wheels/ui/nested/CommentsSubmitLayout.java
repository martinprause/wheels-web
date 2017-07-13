package com.doit.wheels.ui.nested;

import com.doit.wheels.dao.entities.Order;
import com.doit.wheels.services.MessageByLocaleService;
import com.doit.wheels.services.OrderService;
import com.vaadin.data.Binder;
import com.vaadin.ui.*;
import org.vaadin.dialogs.ConfirmDialog;

public class CommentsSubmitLayout extends VerticalLayout {

    private MessageByLocaleService messageByLocaleService;
    private OrderService orderService;

    private Binder<Order> sharedBinder;
    private TextArea commentArea;
    private CheckBox printImmediatelyCheck;
    private final boolean isInEdit;

    public CommentsSubmitLayout(MessageByLocaleService messageByLocaleService,
                                OrderService orderService,
                                Binder<Order> sharedBinder,
                                boolean isInEdit) {
        this.messageByLocaleService = messageByLocaleService;
        this.orderService = orderService;
        this.sharedBinder = sharedBinder;
        this.isInEdit = isInEdit;

        init();
    }

    private void init() {
        this.setHeight("100%");

        HorizontalLayout commentFieldLayout = initCommentFieldLayout();
        HorizontalLayout barCodeCheckLayout = initBarcodeCheckBoxLayout();
        VerticalLayout buttonLayout = initSubmitButtonLayout();

        this.addComponents(commentFieldLayout, barCodeCheckLayout, buttonLayout);
        sharedBinder.forField(commentArea).bind(Order::getComment, Order::setComment);
    }

    private HorizontalLayout initCommentFieldLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("100%");
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        commentArea = new TextArea();
        commentArea.setWidth("75%");
        commentArea.setHeight("300px");
        Label commentLabel = new Label(messageByLocaleService.getMessage("commentSubmitView.comment.label"));
        commentLabel.setId("commentSubmitView.comment.label");
        commentLabel.addStyleName("bold-label");

        layout.addComponents(commentLabel, commentArea);
        layout.setComponentAlignment(commentLabel, Alignment.TOP_RIGHT);
        layout.setComponentAlignment(commentArea, Alignment.MIDDLE_LEFT);
        layout.setExpandRatio(commentLabel, 1);
        layout.setExpandRatio(commentArea, 4);
        return layout;
    }

    private HorizontalLayout initBarcodeCheckBoxLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("100%");
        Label barcodeCheckBoxLabel = new Label(messageByLocaleService.getMessage("commentSubmitView.barcode.label"));
        barcodeCheckBoxLabel.setId("commentSubmitView.barcode.label");
        barcodeCheckBoxLabel.addStyleName("bold-label");
        printImmediatelyCheck = new CheckBox();
        layout.addComponents(barcodeCheckBoxLabel, printImmediatelyCheck);
        layout.setComponentAlignment(barcodeCheckBoxLabel, Alignment.MIDDLE_RIGHT);
        layout.setComponentAlignment(printImmediatelyCheck, Alignment.MIDDLE_LEFT);
        layout.setExpandRatio(printImmediatelyCheck, 4);
        layout.setExpandRatio(barcodeCheckBoxLabel, 1);
        return layout;
    }

    private VerticalLayout initSubmitButtonLayout() {
        String submitOrderButtonLabel = isInEdit ? "commentSubmitView.edit.submit.button" : "commentSubmitView.create.submit.button";
        Button submitOrderButton = new Button(messageByLocaleService.getMessage(submitOrderButtonLabel));
        submitOrderButton.setId(submitOrderButtonLabel);
        submitOrderButton.addStyleName("manage-user-access-buttons");
        submitOrderButton.addClickListener(clickEvent -> submitOrder());
        VerticalLayout buttonLayout = new VerticalLayout();
        buttonLayout.setHeight("400px");

        buttonLayout.addComponent(submitOrderButton);
        buttonLayout.setComponentAlignment(submitOrderButton, Alignment.BOTTOM_CENTER);
        return buttonLayout;
    }

    private void submitOrder() {
        ConfirmDialog.show(getUI(),
                messageByLocaleService.getMessage("commentSubmitView.submitNewOrder.question"),
                (ConfirmDialog.Listener) dialog -> {
                    if (dialog.isConfirmed()) {
                        orderService.save(sharedBinder.getBean());
                        Notification.show(messageByLocaleService.getMessage("commentSubmitView.submitNewOrder.success"),
                                Notification.Type.HUMANIZED_MESSAGE);
                        getUI().getNavigator().navigateTo("customer-orders-list");
                    }
                });
    }
}
