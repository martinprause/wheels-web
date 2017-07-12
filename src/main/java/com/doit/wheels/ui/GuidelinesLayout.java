package com.doit.wheels.ui;

import com.doit.wheels.dao.entities.Guideline;
import com.doit.wheels.dao.entities.Order;
import com.doit.wheels.services.GuidelineService;
import com.doit.wheels.services.MessageByLocaleService;
import com.vaadin.data.Binder;
import com.vaadin.ui.*;

public class GuidelinesLayout extends HorizontalLayout{

    private MessageByLocaleService messageByLocaleService;

    private GuidelineService guidelineService;

    private Binder<Order> binder;

    public GuidelinesLayout(MessageByLocaleService messageByLocaleService, GuidelineService guidelineService, Binder<Order> sharedBinder) {
        this.messageByLocaleService = messageByLocaleService;
        this.guidelineService = guidelineService;
        binder = sharedBinder;
        init();
    }

    public void init() {
        this.setSizeFull();
        this.addStyleName("guidelines");
        initUIComponents();
    }

    private void initUIComponents(){
        VerticalLayout guidelines = new VerticalLayout();
        guidelines.addStyleName("guidelines-table");
        Label guidelinesLabel = new Label(messageByLocaleService.getMessage("guidelines.guidelines.label"));
        guidelinesLabel.setId("guidelines.guidelines.label");
        guidelines.addComponent(guidelinesLabel);

        HorizontalLayout guidelinesComboboxAndButton = new HorizontalLayout();
        ComboBox<Guideline> guidelineComboBox = new ComboBox<>();
        guidelinesComboboxAndButton.addComponent(guidelineComboBox);

        Button addGuideline = new Button(messageByLocaleService.getMessage("guidelines.add.button"));
        addGuideline.setId("guidelines.add.button");
        guidelinesComboboxAndButton.addComponent(addGuideline);
        guidelines.addComponent(guidelinesComboboxAndButton);

        Grid<Guideline> guidelinesTable = new Grid<>();
        guidelinesTable.setColumns("description");
        guidelinesTable.setItems(guidelineService.findAll());

        guidelines.addComponent(guidelinesTable);

        this.addComponent(guidelines);
    }

}
