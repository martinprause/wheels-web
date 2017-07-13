package com.doit.wheels.ui.nested;

import com.doit.wheels.dao.entities.*;
import com.doit.wheels.dao.entities.basic.AbstractModel;
import com.doit.wheels.services.MessageByLocaleService;
import com.doit.wheels.utils.validators.OnlyNumbersValidator;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.provider.GridSortOrder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.HeaderRow;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WheelRimPositionsLayout extends VerticalLayout {

    private final String BUTTON_PREFERRED_SIZE = "175px";
    private int POSITIONS_COUNTER;

    private MessageByLocaleService messageByLocaleService;

    private Binder<WheelRimPosition> localBinder;
    private Binder<Order> sharedBinder;

    private Grid<WheelRimPosition> positionsGrid;

    private ComboBox<Model> modelWheel;
    private ComboBox<ModelType> modelTypeWheel;
    private TextField sizeWheel;
    private CheckBox hubCoverWheel;
    private ComboBox<ValveType> valveTypeWheel;
    private ComboBox<Manufacturer> manufacturerRim;

    private TextField widthRim;
    private TextField heightRim;
    private TextField diameterRim;
    private TextField indexRim;
    private TextField speedRim;

    private Button saveChangedPositionButton;
    private Button addPositionButton;
    private Button duplicateButton;
    private Button editButton;
    private Button deleteButton;

    private ComboBox<Manufacturer> manufacturerWheel;

    private Map<Class, List<? extends AbstractModel>> necessaryEntities;

    public WheelRimPositionsLayout(MessageByLocaleService messageByLocaleService, Binder<Order> sharedBinder, Map<Class, List<? extends AbstractModel>> args) {
        this.messageByLocaleService = messageByLocaleService;
        this.sharedBinder = sharedBinder;
        localBinder = new Binder<>();
        necessaryEntities = args;
        POSITIONS_COUNTER = 0;
        init();
    }

    private void init() {
        initBinderElements();
        initBinders();

        HorizontalLayout wheelRimTablesLayout = new HorizontalLayout();

        VerticalLayout wheelTableLayout = generateWheelLayout();
        wheelRimTablesLayout.setHeight("100%");
        VerticalLayout rimLayout = generateRimLayout();
        rimLayout.setHeight("100%");


        addPositionButton = new Button(messageByLocaleService.getMessage("newOrderView.position.add"));
        addPositionButton.setId("newOrderView.position.add");
        addPositionButton.addStyleName("manage-user-access-buttons");
        addPositionButton.addClickListener(clickEvent -> onNewPositionAdded());
        addPositionButton.setWidth(BUTTON_PREFERRED_SIZE);

        saveChangedPositionButton = new Button(messageByLocaleService.getMessage("customerView.customer.save"));
        saveChangedPositionButton.setId("customerView.customer.save");
        saveChangedPositionButton.addStyleName("manage-user-access-buttons");
        saveChangedPositionButton.addClickListener(clickEvent -> onSaveChangedPosition());
        saveChangedPositionButton.setVisible(false);
        saveChangedPositionButton.setWidth(BUTTON_PREFERRED_SIZE);


        wheelRimTablesLayout.addComponent(wheelTableLayout);
        wheelRimTablesLayout.addComponent(rimLayout);
        wheelRimTablesLayout.addComponents(addPositionButton, saveChangedPositionButton);
        wheelRimTablesLayout.setComponentAlignment(addPositionButton, Alignment.BOTTOM_LEFT);
        wheelRimTablesLayout.setComponentAlignment(saveChangedPositionButton, Alignment.BOTTOM_LEFT);

        initGrid();

        HorizontalLayout controlButtonsLayout = initControlButtonsLayout();

        this.addComponents(wheelRimTablesLayout, positionsGrid, controlButtonsLayout);
        this.setHeight("100%");
    }

    @SuppressWarnings("unchecked")
    private void initBinderElements() {
        manufacturerWheel = new ComboBox<>();
        manufacturerWheel.setEmptySelectionAllowed(false);
        manufacturerWheel.setItems((List<Manufacturer>) necessaryEntities.get(Manufacturer.class));
        manufacturerWheel.setItemCaptionGenerator(Manufacturer::getDescription);

        modelWheel = new ComboBox<>();
        modelWheel.setEmptySelectionAllowed(false);
        modelWheel.setItems((List<Model>)necessaryEntities.get(Model.class));
        modelWheel.setItemCaptionGenerator(Model::getDescription);

        modelTypeWheel = new ComboBox<>();
        modelTypeWheel.setEmptySelectionAllowed(false);
        modelTypeWheel.setItems((List<ModelType>) necessaryEntities.get(ModelType.class));
        modelTypeWheel.setItemCaptionGenerator(ModelType::getDescription);

        sizeWheel = new TextField();
        hubCoverWheel = new CheckBox();

        valveTypeWheel = new ComboBox<>();
        valveTypeWheel.setEmptySelectionAllowed(false);
        valveTypeWheel.setItems((List<ValveType>) necessaryEntities.get(ValveType.class));
        valveTypeWheel.setItemCaptionGenerator(ValveType::getDescription);

        manufacturerRim = new ComboBox<>();
        manufacturerRim.setEmptySelectionAllowed(false);
        manufacturerRim.setItems((List<Manufacturer>) necessaryEntities.get(Manufacturer.class));
        manufacturerRim.setItemCaptionGenerator(Manufacturer::getDescription);

        widthRim = new TextField();
        heightRim = new TextField();
        diameterRim = new TextField();
        indexRim = new TextField();
        speedRim = new TextField();
    }

    private VerticalLayout generateWheelLayout() {
        VerticalLayout wheelLayout = new VerticalLayout();
        Label caption = new Label(messageByLocaleService.getMessage("newOrderView.position.wheel"));
        caption.setId("newOrderView.position.wheel");
        caption.addStyleName("bold-label");
        wheelLayout.addComponent(caption);
        wheelLayout.addStyleName("wheel-rim-layout");

        HorizontalLayout manufacturerWheelLayout = new HorizontalLayout();
        Label manufacturerWheelLabel = new Label(messageByLocaleService.getMessage("newOrderView.position.wheel.manufacturer"));
        manufacturerWheelLabel.addStyleName("user-management-label");
        manufacturerWheelLabel.setId("newOrderView.position.wheel.manufacturer");
        manufacturerWheelLayout.addComponents(manufacturerWheelLabel, manufacturerWheel);

        HorizontalLayout modelWheelLayout = new HorizontalLayout();
        Label modelWheelLabel = new Label(messageByLocaleService.getMessage("newOrderView.position.wheel.model"));
        modelWheelLabel.addStyleName("user-management-label");
        modelWheelLabel.setId("newOrderView.position.wheel.model");
        modelWheelLayout.addComponents(modelWheelLabel, modelWheel);

        HorizontalLayout modelTypeLayout = new HorizontalLayout();
        Label modelTypeLabel = new Label(messageByLocaleService.getMessage("newOrderView.position.wheel.modelType"));
        modelTypeLabel.addStyleName("user-management-label");
        modelTypeLabel.setId("newOrderView.position.wheel.modelType");
        modelTypeLayout.addComponents(modelTypeLabel, modelTypeWheel);

        HorizontalLayout sizeLayout = new HorizontalLayout();
        Label sizeLabel = new Label(messageByLocaleService.getMessage("newOrderView.position.wheel.size"));
        sizeLabel.addStyleName("user-management-label");
        sizeLabel.setId("newOrderView.position.wheel.size");
        sizeLayout.addComponents(sizeLabel, sizeWheel);

        HorizontalLayout hubCoverLayout = new HorizontalLayout();
        hubCoverLayout.addStyleName("hub-cover-layout");
        Label hubCoverLabel = new Label(messageByLocaleService.getMessage("newOrderView.position.wheel.hubCover"));
        hubCoverLabel.addStyleName("user-management-label");
        hubCoverLabel.setId("newOrderView.position.wheel.hubCover");
        hubCoverLayout.addComponents(hubCoverLabel, hubCoverWheel);
        hubCoverLayout.setComponentAlignment(hubCoverWheel, Alignment.MIDDLE_LEFT);

        HorizontalLayout valveTypeLayout = new HorizontalLayout();
        Label valveTypeLabel = new Label(messageByLocaleService.getMessage("newOrderView.position.wheel.valveType"));
        valveTypeLabel.addStyleName("user-management-label");
        valveTypeLabel.setId("newOrderView.position.wheel.valveType");
        valveTypeLayout.addComponents(valveTypeLabel, valveTypeWheel);

        wheelLayout.addComponents(manufacturerWheelLayout, modelWheelLayout, modelTypeLayout,
                sizeLayout, hubCoverLayout, valveTypeLayout);
        return wheelLayout;
    }

    private VerticalLayout generateRimLayout() {
        VerticalLayout rimLayout = new VerticalLayout();
        Label caption = new Label(messageByLocaleService.getMessage("newOrderView.position.rim"));
        caption.setId("newOrderView.position.rim");
        caption.addStyleName("bold-label");
        rimLayout.addComponent(caption);
        rimLayout.addStyleName("wheel-rim-layout");

        HorizontalLayout manufacturerWheelLayout = new HorizontalLayout();
        Label manufacturerWheelLabel = new Label(messageByLocaleService.getMessage("newOrderView.position.wheel.manufacturer"));
        manufacturerWheelLabel.addStyleName("user-management-label");
        manufacturerWheelLabel.setId("newOrderView.position.wheel.manufacturer");
        manufacturerWheelLayout.addComponents(manufacturerWheelLabel, manufacturerRim);

        HorizontalLayout widthLayout = new HorizontalLayout();
        Label widthLabel = new Label(messageByLocaleService.getMessage("newOrderView.position.wheel.width"));
        widthLabel.addStyleName("user-management-label");
        widthLabel.setId("newOrderView.position.wheel.width");
        widthLayout.addComponents(widthLabel, widthRim);

        HorizontalLayout heightLayout = new HorizontalLayout();
        Label heightLabel = new Label(messageByLocaleService.getMessage("newOrderView.position.wheel.height"));
        heightLabel.addStyleName("user-management-label");
        heightLabel.setId("newOrderView.position.wheel.height");
        heightLayout.addComponents(heightLabel, heightRim);

        HorizontalLayout diameterLayout = new HorizontalLayout();
        Label diameterLabel = new Label(messageByLocaleService.getMessage("newOrderView.position.wheel.diameter"));
        diameterLabel.addStyleName("user-management-label");
        diameterLabel.setId("newOrderView.position.wheel.diameter");
        diameterLayout.addComponents(diameterLabel, diameterRim);

        HorizontalLayout indexLayout = new HorizontalLayout();
        Label indexLabel = new Label(messageByLocaleService.getMessage("newOrderView.position.wheel.index"));
        indexLabel.addStyleName("user-management-label");
        indexLabel.setId("newOrderView.position.wheel.index");
        indexLayout.addComponents(indexLabel, indexRim);

        HorizontalLayout speedLayout = new HorizontalLayout();
        Label speedLabel = new Label(messageByLocaleService.getMessage("newOrderView.position.wheel.speed"));
        speedLabel.addStyleName("user-management-label");
        speedLabel.setId("newOrderView.position.wheel.speed");
        speedLayout.addComponents(speedLabel, speedRim);

        rimLayout.addComponents(manufacturerWheelLayout,
                widthLayout,
                heightLayout,
                diameterLayout,
                indexLayout,
                speedLayout);
        return rimLayout;
    }

    private HorizontalLayout initControlButtonsLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("99%");
        layout.setSpacing(false);
        layout.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        duplicateButton = new Button(messageByLocaleService.getMessage("newOrderView.position.buttons.duplicate"));
        duplicateButton.setId("newOrderView.position.buttons.duplicate");
        duplicateButton.addStyleName("manage-user-access-buttons");
        duplicateButton.addClickListener(clickEvent -> onDuplicatePosition());
        duplicateButton.setWidth(BUTTON_PREFERRED_SIZE);

        editButton = new Button(messageByLocaleService.getMessage("newOrderView.position.buttons.edit"));
        editButton.setId("newOrderView.position.buttons.edit");
        editButton.addStyleName("manage-user-access-buttons");
        editButton.addClickListener(clickEvent -> onEditPosition());
        editButton.setWidth(BUTTON_PREFERRED_SIZE);

        deleteButton = new Button(messageByLocaleService.getMessage("newOrderView.position.buttons.delete"));
        deleteButton.setId("newOrderView.position.buttons.delete");
        deleteButton.addStyleName("manage-user-access-buttons");
        deleteButton.addClickListener(clickEvent -> onDeletePosition());
        deleteButton.setWidth(BUTTON_PREFERRED_SIZE);

        duplicateButton.setEnabled(false);
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);

        layout.addComponents(new HorizontalLayout(duplicateButton, editButton), deleteButton);
        layout.setComponentAlignment(deleteButton, Alignment.TOP_RIGHT);
        return layout;
    }

    private void initBinders() {
        StringLengthValidator notEmptyValidator =
                new StringLengthValidator("Can't be empty!", 1, 255);
        OnlyNumbersValidator onlyNumbersValidator =
                new OnlyNumbersValidator("Only numbers allowed!", 0, 255);
        localBinder = new Binder<>();
        localBinder.setBean(new WheelRimPosition());
        localBinder.forField(manufacturerWheel)
                .withValidator(Objects::nonNull,
                        messageByLocaleService.getMessage("newOrderView.position.validationMessages.manufacturer"))
                .bind(WheelRimPosition::getManufacturerWheel, WheelRimPosition::setManufacturerWheel);
        localBinder.forField(modelWheel)
                .withValidator(Objects::nonNull,
                messageByLocaleService.getMessage("newOrderView.position.validationMessages.model"))
                .bind(WheelRimPosition::getModel, WheelRimPosition::setModel);
        localBinder.forField(modelTypeWheel).withValidator(
                Objects::nonNull,
                messageByLocaleService.getMessage("newOrderView.position.validationMessages.modelType"))
                .bind(WheelRimPosition::getModelType, WheelRimPosition::setModelType);
        localBinder.forField(sizeWheel).withValidator(notEmptyValidator)
                .withValidator(onlyNumbersValidator)
                .bind(wheelRimPosition -> format(wheelRimPosition.getSize()),
                (wheelRimPosition1, size) -> wheelRimPosition1.setSize(Integer.valueOf(size)));
        localBinder.forField(hubCoverWheel).bind(WheelRimPosition::getHubCover, WheelRimPosition::setHubCover);
        localBinder.forField(valveTypeWheel).withValidator(Objects::nonNull,
                messageByLocaleService.getMessage("newOrderView.position.validationMessages.valveType"))
                .bind(WheelRimPosition::getValveType, WheelRimPosition::setValveType);
        localBinder.forField(manufacturerRim).withValidator(Objects::nonNull,
                messageByLocaleService.getMessage("newOrderView.position.validationMessages.manufacturer"))
                .bind(WheelRimPosition::getManufacturerRim, WheelRimPosition::setManufacturerRim);
        localBinder.forField(widthRim).withValidator(notEmptyValidator)
                .withValidator(onlyNumbersValidator)
                .bind(wheelRimPosition -> format(wheelRimPosition.getWidth()),
                (wheelRimPosition, s) -> wheelRimPosition.setWidth(Integer.valueOf(s)));
        localBinder.forField(heightRim).withValidator(notEmptyValidator)
                .withValidator(onlyNumbersValidator)
                .bind(wheelRimPosition -> format(wheelRimPosition.getHeight()),
                (wheelRimPosition, s) -> wheelRimPosition.setHeight(Integer.valueOf(s)));
        localBinder.forField(diameterRim).withValidator(notEmptyValidator)
                .withValidator(onlyNumbersValidator)
                .bind(wheelRimPosition -> format(wheelRimPosition.getDiameter()),
                (wheelRimPosition, s) -> wheelRimPosition.setDiameter(Integer.valueOf(s)));
        localBinder.forField(indexRim).withValidator(notEmptyValidator)
                .withValidator(onlyNumbersValidator)
                .bind(wheelRimPosition -> format(wheelRimPosition.getIndexVal()),
                (wheelRimPosition, s) -> wheelRimPosition.setIndexVal(Integer.valueOf(s)));
        localBinder.forField(speedRim).withValidator(notEmptyValidator)
                .bind(WheelRimPosition::getSpeed, WheelRimPosition::setSpeed);
    }

    private void initGrid() {
        positionsGrid = new Grid<>();
        positionsGrid.setWidth("99%");
        positionsGrid.addColumn(WheelRimPosition::getPositionNo).setId("positionNo");
        positionsGrid.addColumn(wheelRimPosition -> wheelRimPosition
                .getManufacturerWheel()
                .getDescription())
                .setId("manufacturerWheel");
        positionsGrid.addColumn(wheelRimPosition -> wheelRimPosition.getModel().getDescription()).setId("model");
        positionsGrid.addColumn(wheelRimPosition -> wheelRimPosition
                .getModelType()
                .getDescription())
                .setId("modelType");
        positionsGrid.addColumn(WheelRimPosition::getSize).setId("size");
        positionsGrid.addColumn(wheelRimPosition -> wheelRimPosition.getHubCover() == null ? "No" :
                wheelRimPosition.getHubCover() ? "Yes" : "No").setId("hubCover");
        positionsGrid.addColumn(wheelRimPosition -> wheelRimPosition.getValveType().getDescription()).setId("valveType");
        positionsGrid.addColumn(wheelRimPosition -> wheelRimPosition
                .getManufacturerRim()
                .getDescription())
                .setId("manufacturerRim");
        positionsGrid.addColumn(WheelRimPosition::getWidth).setId("width");
        positionsGrid.addColumn(WheelRimPosition::getHeight).setId("height");
        positionsGrid.addColumn(WheelRimPosition::getDiameter).setId("diameter");
        positionsGrid.addColumn(WheelRimPosition::getIndexVal).setId("indexVal");
        positionsGrid.addColumn(WheelRimPosition::getSpeed).setId("speed");
        positionsGrid.setSortOrder(GridSortOrder.asc(positionsGrid.getColumn("positionNo")));

        HeaderRow gridHeaderRow = positionsGrid.getDefaultHeaderRow();

        Label positionNoLabel = new Label(messageByLocaleService.getMessage("newOrderView.position.grid.positionNo"));
        positionNoLabel.setId("newOrderView.position.grid.positionNo");
        gridHeaderRow.getCell("positionNo").setComponent(positionNoLabel);

        Label manufacturerWheelLabel = new Label(messageByLocaleService.getMessage("newOrderView.position.grid.manufacturerWheel"));
        manufacturerWheelLabel.setId("newOrderView.position.grid.manufacturerWheel");
        gridHeaderRow.getCell("manufacturerWheel").setComponent(manufacturerWheelLabel);

        Label modelLabel = new Label(messageByLocaleService.getMessage("generalProperties.model"));
        modelLabel.setId("generalProperties.model");
        gridHeaderRow.getCell("model").setComponent(modelLabel);

        Label modelTypeLabel = new Label(messageByLocaleService.getMessage("generalProperties.modelType"));
        modelTypeLabel.setId("generalProperties.modelType");
        gridHeaderRow.getCell("modelType").setComponent(modelTypeLabel);

        Label sizeLabel = new Label(messageByLocaleService.getMessage("newOrderView.position.grid.size"));
        sizeLabel.setId("newOrderView.position.grid.size");
        gridHeaderRow.getCell("size").setComponent(sizeLabel);

        Label hubCoverLabel = new Label(messageByLocaleService.getMessage("newOrderView.position.grid.hubCover"));
        hubCoverLabel.setId("newOrderView.position.grid.hubCover");
        gridHeaderRow.getCell("hubCover").setComponent(hubCoverLabel);

        Label valveTypeLabel = new Label(messageByLocaleService.getMessage("newOrderView.position.grid.valveType"));
        valveTypeLabel.setId("newOrderView.position.grid.valveType");
        gridHeaderRow.getCell("valveType").setComponent(valveTypeLabel);

        Label manufacturerRimLabel = new Label(messageByLocaleService.getMessage("newOrderView.position.grid.manufacturerRim"));
        manufacturerRimLabel.setId("newOrderView.position.grid.manufacturerRim");
        gridHeaderRow.getCell("manufacturerRim").setComponent(manufacturerRimLabel);

        Label widthLabel = new Label(messageByLocaleService.getMessage("newOrderView.position.grid.width"));
        widthLabel.setId("newOrderView.position.grid.width");
        gridHeaderRow.getCell("width").setComponent(widthLabel);

        Label heightLabel = new Label(messageByLocaleService.getMessage("newOrderView.position.grid.height"));
        heightLabel.setId("newOrderView.position.grid.height");
        gridHeaderRow.getCell("height").setComponent(heightLabel);

        Label diameterLabel = new Label(messageByLocaleService.getMessage("newOrderView.position.grid.diameter"));
        diameterLabel.setId("newOrderView.position.grid.diameter");
        gridHeaderRow.getCell("diameter").setComponent(diameterLabel);

        Label indexLabel = new Label(messageByLocaleService.getMessage("newOrderView.position.grid.index"));
        indexLabel.setId("newOrderView.position.grid.index");
        gridHeaderRow.getCell("indexVal").setComponent(indexLabel);

        Label speedLabel = new Label(messageByLocaleService.getMessage("newOrderView.position.grid.speed"));
        speedLabel.setId("newOrderView.position.grid.speed");
        gridHeaderRow.getCell("speed").setComponent(speedLabel);

        Order order = sharedBinder.getBean();
        positionsGrid.setItems(order.getWheelRimPositions());
        positionsGrid.addSelectionListener(selectionEvent -> {
            boolean present = selectionEvent.getFirstSelectedItem().isPresent();
            duplicateButton.setEnabled(present);
            editButton.setEnabled(present);
            deleteButton.setEnabled(present);
        });
    }

    private void onNewPositionAdded() {
        if (validateBindedValues()) {
            WheelRimPosition position = localBinder.getBean();
            Order order = sharedBinder.getBean();
            order.getWheelRimPositions().add(position);
            position.setPositionNo(String.valueOf(++POSITIONS_COUNTER));
            positionsGrid.setItems(order.getWheelRimPositions());
            WheelRimPosition newPosition = new WheelRimPosition();
            localBinder.setBean(newPosition);
        }
    }

    private void onDuplicatePosition() {
        Order bean = sharedBinder.getBean();
        WheelRimPosition wheelRimPosition = positionsGrid.getSelectionModel().getFirstSelectedItem().get();
        WheelRimPosition duplicatedPosition = new WheelRimPosition();
        duplicatedPosition.setManufacturerWheel(wheelRimPosition.getManufacturerWheel());
        duplicatedPosition.setModel(wheelRimPosition.getModel());
        duplicatedPosition.setModelType(wheelRimPosition.getModelType());
        duplicatedPosition.setSize(wheelRimPosition.getSize());
        duplicatedPosition.setHubCover(wheelRimPosition.getHubCover());
        duplicatedPosition.setValveType(wheelRimPosition.getValveType());
        duplicatedPosition.setManufacturerRim(wheelRimPosition.getManufacturerRim());
        duplicatedPosition.setWidth(wheelRimPosition.getWidth());
        duplicatedPosition.setHeight(wheelRimPosition.getHeight());
        duplicatedPosition.setDiameter(wheelRimPosition.getDiameter());
        duplicatedPosition.setIndexVal(wheelRimPosition.getIndexVal());
        duplicatedPosition.setSpeed(wheelRimPosition.getSpeed());
        bean.getWheelRimPositions().add(duplicatedPosition);
        duplicatedPosition.setPositionNo(String.valueOf(Integer.valueOf(wheelRimPosition.getPositionNo()) + 1));
        positionsGrid.setItems(bean.getWheelRimPositions());
        POSITIONS_COUNTER++;
    }

    private void onEditPosition() {
        WheelRimPosition selectedPosition = positionsGrid.getSelectionModel().getFirstSelectedItem().get();
        localBinder.setBean(selectedPosition);
        positionsGrid.deselectAll();
        addPositionButton.setVisible(false);
        saveChangedPositionButton.setCaption(messageByLocaleService.getMessage("customerView.customer.save"));
        saveChangedPositionButton.setVisible(true);
    }

    private void onSaveChangedPosition() {
        if (validateBindedValues()) {
            positionsGrid.setItems(sharedBinder.getBean().getWheelRimPositions());
            saveChangedPositionButton.setVisible(false);
            addPositionButton.setCaption(messageByLocaleService.getMessage("newOrderView.position.add"));
            addPositionButton.setVisible(true);
            localBinder.setBean(new WheelRimPosition());
        }
    }

    private void onDeletePosition() {
        WheelRimPosition wheelRimPosition = positionsGrid.getSelectionModel().getFirstSelectedItem().get();
        sharedBinder.getBean().getWheelRimPositions().remove(wheelRimPosition);
        positionsGrid.setItems(sharedBinder.getBean().getWheelRimPositions());
    }

    private String format(Integer value) {
        return value == null ? null : String.valueOf(value);
    }

    private boolean validateBindedValues() {
        try {
            localBinder.validate();
            localBinder.writeBean(localBinder.getBean());
            return true;
        } catch (ValidationException e) {
            e.printStackTrace();
            return false;
        }
    }
}
