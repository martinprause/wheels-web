package com.doit.wheels.dao.entities;

import com.doit.wheels.dao.entities.basic.Description;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class ValveType extends Description {

    @OneToMany(mappedBy = "valveType")
    @Cascade(CascadeType.ALL)
    private List<WheelRimPosition> wheelRimPositions;

    public List<WheelRimPosition> getWheelRimPositions() {
        return wheelRimPositions;
    }

    public void setWheelRimPositions(List<WheelRimPosition> wheelRimPositions) {
        this.wheelRimPositions = wheelRimPositions;
    }
}
