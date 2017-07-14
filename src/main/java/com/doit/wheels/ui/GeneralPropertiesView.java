package com.doit.wheels.ui;

import com.doit.wheels.dao.entities.*;
import com.doit.wheels.services.*;
import com.doit.wheels.ui.nested.AbstractPropertyLayout;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.Arrays;
import java.util.List;

@Configurable
@SpringComponent
@SpringView(name = "general-properties")
public class GeneralPropertiesView extends VerticalLayout implements View {

    private CssLayout menuBar;

    private final MessageByLocaleService messageService;

    private final CountryService countryService;

    private final ManufacturerService manufacturerService;

    private final ModelService modelService;

    private final ModelTypeService modelTypeService;

    private final ValveTypeService valveTypeService;

    private final GuidelineService guidelineService;

    private Component lastUsedComponent;
    private Button countryButton;
    private Button manufacturerButton;
    private Button modelButton;
    private Button modelTypeButton;
    private Button valveTypeButton;
    private Button guideLineButton;

    @Autowired
    public GeneralPropertiesView(MessageByLocaleService messageService,
                                 CountryService countryService,
                                 ManufacturerService manufacturerService,
                                 ModelService modelService,
                                 ModelTypeService modelTypeService,
                                 ValveTypeService valveTypeService,
                                 GuidelineService guidelineService) {
        this.messageService = messageService;
        this.countryService = countryService;
        this.manufacturerService = manufacturerService;
        this.modelService = modelService;
        this.modelTypeService = modelTypeService;
        this.valveTypeService = valveTypeService;
        this.guidelineService = guidelineService;
    }

    private void init(){
        setSizeFull();

        menuBar = new CssLayout();
        menuBar.setStyleName("general-properties-menu-bar");

        countryButton = new Button(messageService.getMessage("generalProperties.country"));
        countryButton.setId("generalProperties.country");
        countryButton.addStyleName("menu-button");
        countryButton.setIcon(new ThemeResource("img/ico/country.png"));
        countryButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        countryButton.addStyleName("clear-button");

        manufacturerButton = new Button(messageService.getMessage("generalProperties.manufacturer"));
        manufacturerButton.setId("generalProperties.manufacturer");
        manufacturerButton.addStyleName("menu-button");
        manufacturerButton.setIcon(new ThemeResource("img/ico/settings.png"));
        manufacturerButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        manufacturerButton.addStyleName("clear-button");

        modelButton = new Button(messageService.getMessage("generalProperties.model"));
        modelButton.setId("generalProperties.model");
        modelButton.addStyleName("menu-button");
        modelButton.setIcon(new ThemeResource("img/ico/model.png"));
        modelButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        modelButton.addStyleName("clear-button");

        modelTypeButton = new Button(messageService.getMessage("generalProperties.modelType"));
        modelTypeButton.setId("generalProperties.modelType");
        modelTypeButton.addStyleName("menu-button");
        modelTypeButton.setIcon(new ThemeResource("img/ico/model-type.png"));
        modelTypeButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        modelTypeButton.addStyleName("clear-button");

        valveTypeButton = new Button(messageService.getMessage("generalProperties.valveType"));
        valveTypeButton.setId("generalProperties.valveType");
        valveTypeButton.addStyleName("menu-button");
        valveTypeButton.setIcon(new ThemeResource("img/ico/valve-type.png"));
        valveTypeButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        valveTypeButton.addStyleName("clear-button");

        guideLineButton = new Button(messageService.getMessage("generalProperties.guideline"));
        guideLineButton.setId("generalProperties.guideline");
        guideLineButton.addStyleName("menu-button");
        guideLineButton.setIcon(new ThemeResource("img/ico/guideline.png"));
        guideLineButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        guideLineButton.addStyleName("clear-button");

        menuBar.addComponent(countryButton);
        menuBar.addComponent(manufacturerButton);
        menuBar.addComponent(modelButton);
        menuBar.addComponent(modelTypeButton);
        menuBar.addComponent(valveTypeButton);
        menuBar.addComponent(guideLineButton);

        AbstractPropertyLayout<Country> countryView = new AbstractPropertyLayout<>(messageService, countryService, Country.class);

        lastUsedComponent = countryView;

        countryButton.addClickListener(clickEvent -> {
            makeButtonSelected(countryButton);
            replaceComponent(new AbstractPropertyLayout<>(messageService, countryService, Country.class));
        });
        manufacturerButton.addClickListener(clickEvent ->{
            makeButtonSelected(manufacturerButton);
            replaceComponent(new AbstractPropertyLayout<>(messageService, manufacturerService, Manufacturer.class));
        });
        modelButton.addClickListener(clickEvent -> {
            makeButtonSelected(modelButton);
            replaceComponent(new AbstractPropertyLayout<>(messageService, modelService, Model.class));
        });
        modelTypeButton.addClickListener(clickEvent -> {
            makeButtonSelected(modelTypeButton);
            replaceComponent(new AbstractPropertyLayout<>(messageService, modelTypeService, ModelType.class));
        });
        valveTypeButton.addClickListener(clickEvent -> {
            makeButtonSelected(valveTypeButton);
            replaceComponent(new AbstractPropertyLayout<>(messageService, valveTypeService, ValveType.class));
        });
        guideLineButton.addClickListener(clickEvent -> {
            makeButtonSelected(guideLineButton);
            replaceComponent(new AbstractPropertyLayout<>(messageService, guidelineService, Guideline.class));
        });

        this.addComponent(menuBar);
        this.addComponent(countryView);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        init();
    }

    private void replaceComponent(Component newComponent) {
        replaceComponent(lastUsedComponent, newComponent);
        lastUsedComponent = newComponent;
    }

    private void makeButtonSelected(Button buttonToSelect) {
        List<Button> buttons = Arrays.asList(countryButton, manufacturerButton, modelButton, modelTypeButton, guideLineButton, valveTypeButton);
        buttons.forEach(button -> button.removeStyleName("selected"));
        buttonToSelect.addStyleName("selected");
    }
}
