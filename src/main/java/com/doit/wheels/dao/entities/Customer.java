package com.doit.wheels.dao.entities;

import com.doit.wheels.dao.entities.basic.Contact;

import javax.persistence.Entity;

@Entity
public class Customer extends Contact {
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
