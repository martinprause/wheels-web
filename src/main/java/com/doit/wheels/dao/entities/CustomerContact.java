package com.doit.wheels.dao.entities;

import com.doit.wheels.dao.entities.basic.Contact;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class CustomerContact extends Contact {

    @ManyToOne
    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
