package com.doit.wheels.ui.nested;

import com.doit.wheels.dao.entities.Guideline;
import com.doit.wheels.dao.entities.Order;
import com.doit.wheels.dao.entities.basic.Description;
import com.doit.wheels.services.GuidelineService;
import com.doit.wheels.services.MessageByLocaleService;
import com.vaadin.data.Binder;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GuidelinesLayout extends HorizontalLayout{

    private MessageByLocaleService messageByLocaleService;

    private GuidelineService guidelineService;

    private Binder<Order> binder;

    private Order order;

    private Grid<Guideline> guidelinesTable;

    private ComboBox<Guideline> guidelineComboBox;


    public GuidelinesLayout(MessageByLocaleService messageByLocaleService, GuidelineService guidelineService,
                            Binder<Order> sharedBinder) {
        this.messageByLocaleService = messageByLocaleService;
        this.guidelineService = guidelineService;
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
        guidelinesComboboxAndButton.addStyleName("under-table-panel");
        guidelineComboBox = new ComboBox<>();
        guidelineComboBox.setItems(guidelineService.findAll());
        guidelineComboBox.setItemCaptionGenerator(Description::getDescription);
        guidelineComboBox.setEmptySelectionAllowed(false);
        guidelineComboBox.addStyleName("guideline-combobox");
        guidelinesComboboxAndButton.addComponent(guidelineComboBox);

        Button addGuideline = new Button(messageByLocaleService.getMessage("guidelines.add.button"));
        addGuideline.addStyleName("theme-buttons");
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
        deleteGuidelineButton.addStyleName("theme-buttons");
        deleteGuidelineButton.addClickListener(e -> deleteGuideline());
        deleteGuidelineButton.setId("guidelines.delete.button");
        deleteGuidelineButton.setWidth("175px");
        deleteButtonLayout.addComponent(deleteGuidelineButton);

        guidelines.addComponent(deleteButtonLayout);
        this.addComponent(guidelines);

    }

    private void showPictures() {
        this.addComponent(initPictureList());

        ThemeResource noImageResource = new ThemeResource("img/no_image.png");
        VerticalLayout signatureLayout = new VerticalLayout();
        Label signatureLabel = new Label(messageByLocaleService.getMessage("guidelines.signature.label"));
        signatureLabel.setId("guidelines.signature.label");
        signatureLayout.addComponent(signatureLabel);
        signatureLayout.addStyleName("guideline-signature-layout");

        Image signatureImage = new Image();
        signatureImage.setSource(noImageResource);
        signatureImage.setWidth("100%");
        signatureImage.setHeight("100%");
        signatureLayout.addComponent(signatureImage);
        this.addComponent(signatureLayout);

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
            order.getGuidelines().remove(selectedGuideline);
            updateGuidelinesGrid();
        }

    }

    private void addGuideline() {
        Guideline selectedGuideline = guidelineComboBox.getSelectedItem().get();
        order.getGuidelines().add(selectedGuideline);
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

    private VerticalLayout initPictureList() {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.addStyleName("guideline-picture-layout");
        Label pictureLabel = new Label(messageByLocaleService.getMessage("guidelines.picture.label"));
        pictureLabel.setId("guidelines.picture.label");
        mainLayout.addComponent(pictureLabel);

        VerticalLayout innerLayout = new VerticalLayout();
        innerLayout.setMargin(false);

        List<byte[]> images = new ArrayList<>();
        Panel scroll = new Panel(innerLayout);
        scroll.setHeight("365px");
        scroll.addStyleName("customer-details-panel");

        mainLayout.addComponent(scroll);
        if (order.getId() != null) {
            images.add(order.getWheelsRimPicture1());
            images.add(order.getWheelsRimPicture2());
            images.add(order.getWheelsRimPicture3());
            images.add(order.getWheelsRimPicture4());
            images.forEach(image -> {
                if (image != null) {
                    Image pictureImage = new Image();
                    pictureImage.setWidth("100%");
                    pictureImage.setHeight("100%");
                    StreamResource.StreamSource imageSourceForPicture = new ImageSource(order.getWheelsRimPicture1());
                    StreamResource resourceForPicture =
                            new StreamResource(imageSourceForPicture, "wheel.jpg");
                    pictureImage.setSource(resourceForPicture);
                    innerLayout.addComponent(pictureImage);
                }
            });

            return mainLayout;
        } else {
            Image pictureImage = new Image();
            pictureImage.setWidth("100%");
            pictureImage.setHeight("100%");
            pictureImage.setSource(new ThemeResource("img/no_image.png"));

            return mainLayout;
        }
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
