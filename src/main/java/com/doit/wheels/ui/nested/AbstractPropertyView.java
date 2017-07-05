package com.doit.wheels.ui.nested;

import com.doit.wheels.dao.entities.Description;
import com.doit.wheels.services.GenericService;
import com.doit.wheels.services.MessageByLocaleService;
import com.vaadin.data.Binder;
import com.vaadin.ui.*;

public class AbstractPropertyView<T extends Description> extends HorizontalLayout {

    private GenericService<T> entityService;
    private MessageByLocaleService messageByLocaleService;
    private Class<T> type;

    private TextField description;
    private Grid<T> entityGrid;

    private Binder<T> entityBinder;

    public AbstractPropertyView(MessageByLocaleService messageByLocaleService, GenericService<T> entityService, Class<T> type) {
        this.messageByLocaleService = messageByLocaleService;
        this.type = type;
        this.entityService = entityService;
        entityBinder = new Binder<>(type);
        init();
    }

    private void init(){
        setSizeFull();
//        this.addStyleName("general-properties-content");
        VerticalLayout leftPart = new VerticalLayout();
        Label entityName = new Label(messageByLocaleService.getMessage(getLabelCodeByType()));
        entityName.setId(getLabelCodeByType());
        leftPart.addComponent(entityName);

        description = new TextField();
        description.setPlaceholder(messageByLocaleService.getMessage("generalProperties.description"));
        description.setId("generalProperties.description");
        leftPart.addComponent(description);


        VerticalLayout middlePart = new VerticalLayout();
        middlePart.setStyleName("gen-properties-middle-layout");

        Button addEntityButton = new Button(messageByLocaleService.getMessage("generalProperties.buttons.add"));
        addEntityButton.setId("generalProperties.buttons.add");
        addEntityButton.setWidth("110px");
        addEntityButton.addClickListener(clickEvent -> saveEntity());

        Button editEntityButton = new Button(messageByLocaleService.getMessage("generalProperties.buttons.edit"));
        editEntityButton.addClickListener(clickEvent -> updateEntity());
        editEntityButton.setId("generalProperties.buttons.edit");
        editEntityButton.setWidth("110px");
        editEntityButton.setEnabled(false);

        Button deleteEntityButton = new Button(messageByLocaleService.getMessage("generalProperties.buttons.delete"));
        deleteEntityButton.addClickListener(clickEvent -> deleteEntity());
        deleteEntityButton.setWidth("110px");
        deleteEntityButton.setId("generalProperties.buttons.delete");
        deleteEntityButton.setEnabled(false);

        middlePart.addComponent(addEntityButton);
        middlePart.addComponent(editEntityButton);
        middlePart.addComponent(deleteEntityButton);

        VerticalLayout rightPart = new VerticalLayout();

        entityGrid = new Grid<>(type);
        entityGrid.setColumns("description");
        Label gridHeaderLabel = new Label(messageByLocaleService.getMessage("generalProperties.grid.column.caption"));
        gridHeaderLabel.setId("generalProperties.grid.column.caption");
        entityGrid.getDefaultHeaderRow().getCell("description").setComponent(gridHeaderLabel);
        entityGrid.setId("generalProperties.grid.column.caption");
        entityGrid.setItems(entityService.findAll());
        entityGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        entityGrid.addSelectionListener(selectionEvent -> {
            if (selectionEvent.getFirstSelectedItem().isPresent()) {
                T t = selectionEvent.getFirstSelectedItem().get();
                initBinder(t);
                editEntityButton.setEnabled(true);
                deleteEntityButton.setEnabled(true);
                addEntityButton.setEnabled(false);
            } else {
                initBinder();
                editEntityButton.setEnabled(false);
                deleteEntityButton.setEnabled(false);
                addEntityButton.setEnabled(true);
            }
        });
        Label gridCapture = new Label(messageByLocaleService.getMessage(getGridCaptionLabelByType()));
        gridCapture.setId(getGridCaptionLabelByType());
        rightPart.addComponent(gridCapture);
        rightPart.addComponent(entityGrid);
        initBinder();

        HorizontalLayout layout = new HorizontalLayout();
        layout.addStyleName("general-properties-content");
        layout.addComponent(leftPart);
        layout.addComponent(middlePart);
        layout.addComponent(rightPart);

        this.addComponent(layout);

        entityBinder.bindInstanceFields(this);
    }

    private String getLabelCodeByType(){
        final String country = "Country";
        final String manufacturer = "Manufacturer";
        final String model = "Model";
        final String modelType = "ModelType";
        final String valveType = "ValveType";
        final String guideline = "Guideline";
        switch (type.getSimpleName()) {
            case country: {
                return "generalProperties.country";
            }
            case manufacturer: {
                return "generalProperties.manufacturer";
            }
            case model: {
                return "generalProperties.model";
            }
            case modelType: {
                return "generalProperties.modelType";
            }
            case valveType: {
                return "generalProperties.valveType";
            }
            case guideline: {
                return "generalProperties.guideline";
            }
            default:
                return "";
        }
    }

    private String getGridCaptionLabelByType(){
        final String country = "Country";
        final String manufacturer = "Manufacturer";
        final String model = "Model";
        final String modelType = "ModelType";
        final String valveType = "ValveType";
        final String guideline = "Guideline";
        switch (type.getSimpleName()) {
            case country: {
                return "generalProperties.grid.caption.countries";
            }
            case manufacturer: {
                return "generalProperties.grid.caption.manufacturers";
            }
            case model: {
                return "generalProperties.grid.caption.models";
            }
            case modelType: {
                return "generalProperties.grid.caption.modelTypes";
            }
            case valveType: {
                return "generalProperties.grid.caption.valveTypes";
            }
            case guideline: {
                return "generalProperties.grid.caption.guidelines";
            }
            default:
                return "";
        }
    }

    private void saveEntity(){
        T entity = entityBinder.getBean();
        entityService.save(entity);
        entityGrid.setItems(entityService.findAll());
        initBinder();
    }

    private void updateEntity() {
        T entityToSave = entityBinder.getBean();
        entityService.update(entityToSave);
        entityGrid.setItems(entityService.findAll());
        initBinder();
    }

    private void deleteEntity() {
        T bean = entityBinder.getBean();
        entityService.delete(bean);
        entityGrid.setItems(entityService.findAll());
        initBinder();
    }

    @SafeVarargs
    private final void initBinder(T... t) {
        try {
            if(t.length > 0) {
                entityBinder.setBean(t[0]);
            } else {
                entityBinder.setBean(type.newInstance());
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
