package com.doit.wheels.dao.entities;

import com.doit.wheels.dao.entities.basic.Description;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Manufacturer extends Description {

    @OneToMany(mappedBy = "manufacturerWheel", fetch = FetchType.LAZY)
    @Cascade(CascadeType.ALL)
    private List<WheelRimPosition> wheelRimPositionsByWheel;

    @OneToMany(mappedBy = "manufacturerRim", fetch = FetchType.LAZY)
    @Cascade(CascadeType.ALL)
    private List<WheelRimPosition> wheelRimPositionsByRim;

    public List<WheelRimPosition> getWheelRimPositionsByRim() {
        return wheelRimPositionsByRim;
    }

    public void setWheelRimPositionsByRim(List<WheelRimPosition> wheelRimPositionsByRim) {
        this.wheelRimPositionsByRim = wheelRimPositionsByRim;
    }

    public List<WheelRimPosition> getWheelRimPositionsByWheel() {
        return wheelRimPositionsByWheel;
    }

    public void setWheelRimPositionsByWheel(List<WheelRimPosition> wheelRimPositionsByWheel) {
        this.wheelRimPositionsByWheel = wheelRimPositionsByWheel;
    }
}
