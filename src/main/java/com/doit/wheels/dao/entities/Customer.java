package com.doit.wheels.dao.entities;

import com.doit.wheels.dao.entities.basic.Contact;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Customer extends Contact implements Cloneable{
    private String comment;

    @OneToMany(mappedBy = "customer")
    @Cascade(CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;

        Customer customer = (Customer) o;

        if (!super.equals(o)) return false;

        if (comment != null ? !comment.equals(customer.comment) : customer.comment != null) return false;
        if (customerContacts != null ? !customerContacts.equals(customer.customerContacts) : customer.customerContacts != null)
            return false;
//        if (this.getLastname() != null ? !this.getLastname().equals(customer.getLastname()) : customer.getLastname() != null) return false;
        return orders != null ? orders.equals(customer.orders) : customer.orders == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (customerContacts != null ? customerContacts.hashCode() : 0);
        result = 31 * result + (orders != null ? orders.hashCode() : 0);
        return result;
    }
}
