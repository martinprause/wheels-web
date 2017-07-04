package com.doit.wheels.ui;

import com.doit.wheels.dao.entities.*;
import com.doit.wheels.services.*;
import com.doit.wheels.ui.nested.AbstractPropertyView;
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

@Configurable
@SpringComponent
@SpringView(name = "general-properties")
public class GeneralPropertiesView extends VerticalLayout implements View {

    private CssLayout menuBar;

    @Autowired
    private MessageByLocaleService messageService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelTypeService modelTypeService;

    @Autowired
    private ValveTypeService valveTypeService;

    @Autowired
    private GuidelineService guidelineService;

    private Component lastUsedComponent;

    private void init(){
        setSizeFull();

        menuBar = new CssLayout();
        menuBar.setStyleName("general-properties-menu-bar");

        Button countryButton = new Button(messageService.getMessage("generalProperties.country"));
        countryButton.setId("generalProperties.country");
        countryButton.addStyleName("menu-button");
        countryButton.setIcon(new ThemeResource("img/ico/country.png"));
        countryButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        countryButton.addStyleName("clear-button");

        Button manufacturerButton = new Button(messageService.getMessage("generalProperties.manufacturer"));
        manufacturerButton.setId("generalProperties.manufacturer");
        manufacturerButton.addStyleName("menu-button");
        manufacturerButton.setIcon(new ThemeResource("img/ico/settings.png"));
        manufacturerButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        manufacturerButton.addStyleName("clear-button");

        Button modelButton = new Button(messageService.getMessage("generalProperties.model"));
        modelButton.setId("generalProperties.model");
        modelButton.addStyleName("menu-button");
        modelButton.setIcon(new ThemeResource("img/ico/model.png"));
        modelButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        modelButton.addStyleName("clear-button");

        Button modelTypeButton = new Button(messageService.getMessage("generalProperties.modelType"));
        modelTypeButton.setId("generalProperties.modelType");
        modelTypeButton.addStyleName("menu-button");
        modelTypeButton.setIcon(new ThemeResource("img/ico/model-type.png"));
        modelTypeButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        modelTypeButton.addStyleName("clear-button");

        Button valveTypeButton = new Button(messageService.getMessage("generalProperties.valveType"));
        valveTypeButton.setId("generalProperties.valveType");
        valveTypeButton.addStyleName("menu-button");
        valveTypeButton.setIcon(new ThemeResource("img/ico/valve-type.png"));
        valveTypeButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        valveTypeButton.addStyleName("clear-button");

        Button guideLineButton = new Button(messageService.getMessage("generalProperties.guideline"));
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

        AbstractPropertyView<Country> countryView = new AbstractPropertyView<>(messageService, countryService, Country.class);

        lastUsedComponent = countryView;

        countryButton.addClickListener(clickEvent -> replaceComponent(new AbstractPropertyView<>(messageService, countryService, Country.class)));
        manufacturerButton.addClickListener(clickEvent -> replaceComponent(new AbstractPropertyView<>(messageService, manufacturerService, Manufacturer.class)));
        modelButton.addClickListener(clickEvent -> replaceComponent(new AbstractPropertyView<>(messageService, modelService, Model.class)));
        modelTypeButton.addClickListener(clickEvent -> replaceComponent(new AbstractPropertyView<>(messageService, modelTypeService, ModelType.class)));
        valveTypeButton.addClickListener(clickEvent -> replaceComponent(new AbstractPropertyView<>(messageService, valveTypeService, ValveType.class)));
        guideLineButton.addClickListener(clickEvent -> replaceComponent(new AbstractPropertyView<>(messageService, guidelineService, Guideline.class)));

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
}
