package com.doit.wheels.dao.entities;

import com.doit.wheels.dao.entities.basic.Contact;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Customer extends Contact {
    private String comment;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    private List<CustomerContact> customerContacts;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;

    public Customer() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<CustomerContact> getCustomerContacts() {
        return customerContacts;
    }

    public void setCustomerContacts(List<CustomerContact> customerContacts) {
        this.customerContacts = customerContacts;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public String getFullName(){
        return this.getFirstname() + " " + this.getLastname();
    }
}
