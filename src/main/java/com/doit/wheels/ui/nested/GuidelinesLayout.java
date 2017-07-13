package com.doit.wheels.ui.nested;

import com.doit.wheels.dao.entities.Guideline;
import com.doit.wheels.dao.entities.Order;
import com.doit.wheels.dao.entities.basic.Description;
import com.doit.wheels.services.GuidelineService;
import com.doit.wheels.services.MessageByLocaleService;
import com.doit.wheels.services.OrderService;
import com.vaadin.data.Binder;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class GuidelinesLayout extends HorizontalLayout{

    private MessageByLocaleService messageByLocaleService;

    private GuidelineService guidelineService;

    private Binder<Order> binder;

    private Order order;

    private Grid<Guideline> guidelinesTable;

    private ComboBox<Guideline> guidelineComboBox;

    private Image pictureImage;

    private Image signatureImage;

    private OrderService orderService;


    public GuidelinesLayout(MessageByLocaleService messageByLocaleService, GuidelineService guidelineService,
                            Binder<Order> sharedBinder, OrderService orderService) {
        this.messageByLocaleService = messageByLocaleService;
        this.guidelineService = guidelineService;
        this.orderService = orderService;
        binder = sharedBinder;
        init();
    }

    public void init() {
        this.setSizeFull();
        this.addStyleName("guidelines");
        initUIComponents();
        initBinderAndValidation();
        showPictures();
    }

    private void initUIComponents(){
        VerticalLayout guidelines = new VerticalLayout();
        guidelines.addStyleName("guidelines-table");
        Label guidelinesLabel = new Label(messageByLocaleService.getMessage("guidelines.guidelines.label"));
        guidelinesLabel.setId("guidelines.guidelines.label");
        guidelines.addComponent(guidelinesLabel);

        HorizontalLayout guidelinesComboboxAndButton = new HorizontalLayout();
        guidelineComboBox = new ComboBox<>();
        guidelineComboBox.setItems(guidelineService.findAll());
        guidelineComboBox.setItemCaptionGenerator(Description::getDescription);
        guidelineComboBox.setEmptySelectionAllowed(false);
        guidelinesComboboxAndButton.addComponent(guidelineComboBox);

        Button addGuideline = new Button(messageByLocaleService.getMessage("guidelines.add.button"));
        addGuideline.setId("guidelines.add.button");
        addGuideline.addClickListener(e -> addGuideline());
        addGuideline.setWidth("175px");
        guidelinesComboboxAndButton.addComponent(addGuideline);
        guidelines.addComponent(guidelinesComboboxAndButton);

        guidelinesTable = new Grid<>(Guideline.class);
        guidelinesTable.setColumns("description");
        guidelinesTable.setWidth("95%");
        guidelinesTable.setHeight("250px");
        guidelines.addComponent(guidelinesTable);

        HorizontalLayout deleteButtonLayout = new HorizontalLayout();
        deleteButtonLayout.addStyleName("guideline-delete-layout");
        Button deleteGuidelineButton = new Button(messageByLocaleService.getMessage("guidelines.delete.button"));
        deleteGuidelineButton.addClickListener(e -> deleteGuideline());
        deleteGuidelineButton.setId("guidelines.delete.button");
        deleteGuidelineButton.setWidth("175px");
        deleteButtonLayout.addComponent(deleteGuidelineButton);

        guidelines.addComponent(deleteButtonLayout);
        this.addComponent(guidelines);

        VerticalLayout pictureLayout = new VerticalLayout();
        pictureLayout.addStyleName("guideline-picture-layout");
        Label pictureLabel = new Label(messageByLocaleService.getMessage("guidelines.picture.label"));
        pictureLabel.setId("guidelines.picture.label");
        pictureLayout.addComponent(pictureLabel);

        ThemeResource noImageResource = new ThemeResource("img/no_image.png");
        pictureImage = new Image();
        pictureImage.setSource(noImageResource);
        pictureImage.setWidth("100%");
        pictureImage.setHeight("100%");
        pictureLayout.addComponent(pictureImage);
        this.addComponent(pictureLayout);

        VerticalLayout signatureLayout = new VerticalLayout();
        Label signatureLabel = new Label(messageByLocaleService.getMessage("guidelines.signature.label"));
        signatureLabel.setId("guidelines.signature.label");
        signatureLayout.addComponent(signatureLabel);
        signatureLayout.addStyleName("guideline-signature-layout");
        signatureImage = new Image();
        signatureImage.setSource(noImageResource);
        signatureImage.setWidth("100%");
        signatureImage.setHeight("100%");
        signatureLayout.addComponent(signatureImage);
        this.addComponent(signatureLayout);

    }

    private void showPictures() {
        if (order.getWheelsRimPicture() != null){
            StreamResource.StreamSource imageSourceForPicture = new ImageSource(order.getWheelsRimPicture());
            StreamResource resourceForPicture =
                    new StreamResource(imageSourceForPicture, "wheel.jpg");
            pictureImage.setSource(resourceForPicture);
        }

        if (order.getSignaturePicture() != null){
            StreamResource.StreamSource imageSourceForSignature = new ImageSource(order.getSignaturePicture());
            StreamResource resourceForSignature =
                    new StreamResource(imageSourceForSignature, "wheel.jpg");
            signatureImage.setSource(resourceForSignature);
        }

    }

    private void deleteGuideline() {
        if (guidelinesTable.getSelectedItems().iterator().hasNext()){
            Guideline selectedGuideline = guidelinesTable.getSelectedItems().iterator().next();
            selectedGuideline.getOrders().remove(order);
            order.getGuidelines().remove(selectedGuideline);
            guidelineService.save(selectedGuideline);
            updateGuidelinesGrid();
        }

    }

    private void addGuideline() {
        Guideline selectedGuideline = guidelineComboBox.getSelectedItem().get();
        selectedGuideline.getOrders().add(order);
        order.getGuidelines().add(selectedGuideline);
        guidelineService.save(selectedGuideline);
        updateGuidelinesGrid();
    }

    private void initBinderAndValidation(){
        if (binder.getBean() != null) {
            order = binder.getBean();
        } else {
            order = new Order();
        }
        updateGuidelinesGrid();
    }

    private void updateGuidelinesGrid(){
        guidelinesTable.setItems(order.getGuidelines());
    }

    private class ImageSource implements StreamResource.StreamSource{

        private byte[] image;

        @Override
        public InputStream getStream() {
            return new ByteArrayInputStream(image);
        }

        ImageSource(byte[] image){
            this.image = image;
            getStream();
        }
    }

}
