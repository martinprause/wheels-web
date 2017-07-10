package com.doit.wheels.dao.entities;

import com.doit.wheels.dao.entities.basic.Description;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class ModelType extends Description {

    @OneToMany(mappedBy = "modelType")
    @Cascade(CascadeType.ALL)
    private List<WheelRimPosition> wheelRimPositions;

    public List<WheelRimPosition> getWheelRimPositions() {
        return wheelRimPositions;
    }

    public void setWheelRimPositions(List<WheelRimPosition> wheelRimPositions) {
        this.wheelRimPositions = wheelRimPositions;
    }
}
