package com.doit.wheels.dao.entities;

import com.doit.wheels.dao.entities.basic.AbstractModel;
import com.doit.wheels.utils.enums.StatusTypeEnum;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Entity
public class WheelRimPosition extends AbstractModel {

    private String positionNo;

    @ManyToOne
    private Manufacturer manufacturerWheel;

    @ManyToOne
    private Model model;

    @ManyToOne
    private ModelType modelType;
    private Integer size;
    private Boolean hubCover;

    @ManyToOne
    private ValveType valveType;

    @ManyToOne
    private Manufacturer manufacturerRim;

    @Enumerated(EnumType.STRING)
    private StatusTypeEnum status;
    private Integer width;
    private Integer height;
    private Integer diameter;
    private Integer indexVal;
    private String speed;

    private String qrCode;

    public WheelRimPosition() {}

    public WheelRimPosition(WheelRimPosition other) {
        this.positionNo = other.positionNo;
        this.manufacturerWheel = other.manufacturerWheel;
        this.model = other.model;
        this.modelType = other.modelType;
        this.size = other.size;
        this.hubCover = other.hubCover;
        this.valveType = other.valveType;
        this.manufacturerRim = other.manufacturerRim;
        this.status = other.status;
        this.width = other.width;
        this.height = other.height;
        this.diameter = other.diameter;
        this.indexVal = other.indexVal;
        this.speed = other.speed;
        this.qrCode = other.qrCode;
    }

    public String getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(String positionNo) {
        this.positionNo = positionNo;
    }

    public Manufacturer getManufacturerWheel() {
        return manufacturerWheel;
    }

    public void setManufacturerWheel(Manufacturer manufacturerWheel) {
        this.manufacturerWheel = manufacturerWheel;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public ModelType getModelType() {
        return modelType;
    }

    public void setModelType(ModelType modelType) {
        this.modelType = modelType;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Boolean getHubCover() {
        return hubCover;
    }

    public void setHubCover(Boolean hubCover) {
        this.hubCover = hubCover;
    }

    public ValveType getValveType() {
        return valveType;
    }

    public void setValveType(ValveType valveType) {
        this.valveType = valveType;
    }

    public Manufacturer getManufacturerRim() {
        return manufacturerRim;
    }

    public void setManufacturerRim(Manufacturer manufacturerRim) {
        this.manufacturerRim = manufacturerRim;
    }

    public StatusTypeEnum getStatus() {
        return status;
    }

    public void setStatus(StatusTypeEnum status) {
        this.status = status;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getDiameter() {
        return diameter;
    }

    public void setDiameter(Integer diameter) {
        this.diameter = diameter;
    }

    public Integer getIndexVal() {
        return indexVal;
    }

    public void setIndexVal(Integer indexVal) {
        this.indexVal = indexVal;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WheelRimPosition)) return false;

        WheelRimPosition that = (WheelRimPosition) o;

        if (getPositionNo() != null ? !getPositionNo().equals(that.getPositionNo()) : that.getPositionNo() != null)
            return false;
        if (getManufacturerWheel() != null ? !getManufacturerWheel().equals(that.getManufacturerWheel()) : that.getManufacturerWheel() != null)
            return false;
        if (getModel() != null ? !getModel().equals(that.getModel()) : that.getModel() != null) return false;
        if (getModelType() != null ? !getModelType().equals(that.getModelType()) : that.getModelType() != null)
            return false;
        if (getSize() != null ? !getSize().equals(that.getSize()) : that.getSize() != null) return false;
        if (getHubCover() != null ? !getHubCover().equals(that.getHubCover()) : that.getHubCover() != null)
            return false;
        if (getValveType() != null ? !getValveType().equals(that.getValveType()) : that.getValveType() != null)
            return false;
        if (getManufacturerRim() != null ? !getManufacturerRim().equals(that.getManufacturerRim()) : that.getManufacturerRim() != null)
            return false;
        if (getStatus() != that.getStatus()) return false;
        if (getWidth() != null ? !getWidth().equals(that.getWidth()) : that.getWidth() != null) return false;
        if (getHeight() != null ? !getHeight().equals(that.getHeight()) : that.getHeight() != null) return false;
        if (getDiameter() != null ? !getDiameter().equals(that.getDiameter()) : that.getDiameter() != null)
            return false;
        if (getIndexVal() != null ? !getIndexVal().equals(that.getIndexVal()) : that.getIndexVal() != null)
            return false;
        if (getSpeed() != null ? !getSpeed().equals(that.getSpeed()) : that.getSpeed() != null) return false;
        return getQrCode() != null ? getQrCode().equals(that.getQrCode()) : that.getQrCode() == null;
    }

    @Override
    public int hashCode() {
        int result = getPositionNo() != null ? getPositionNo().hashCode() : 0;
        result = 31 * result + (getManufacturerWheel() != null ? getManufacturerWheel().hashCode() : 0);
        result = 31 * result + (getModel() != null ? getModel().hashCode() : 0);
        result = 31 * result + (getModelType() != null ? getModelType().hashCode() : 0);
        result = 31 * result + (getSize() != null ? getSize().hashCode() : 0);
        result = 31 * result + (getHubCover() != null ? getHubCover().hashCode() : 0);
        result = 31 * result + (getValveType() != null ? getValveType().hashCode() : 0);
        result = 31 * result + (getManufacturerRim() != null ? getManufacturerRim().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getWidth() != null ? getWidth().hashCode() : 0);
        result = 31 * result + (getHeight() != null ? getHeight().hashCode() : 0);
        result = 31 * result + (getDiameter() != null ? getDiameter().hashCode() : 0);
        result = 31 * result + (getIndexVal() != null ? getIndexVal().hashCode() : 0);
        result = 31 * result + (getSpeed() != null ? getSpeed().hashCode() : 0);
        result = 31 * result + (getQrCode() != null ? getQrCode().hashCode() : 0);
        return result;
    }
}
