package com.doit.wheels.dao.entities.basic;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Description extends AbstractModel {

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
