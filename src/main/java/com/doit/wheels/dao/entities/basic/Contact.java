package com.doit.wheels.dao.entities.basic;

import com.doit.wheels.dao.entities.Country;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Contact extends AbstractModel {

    private String customerNo;
    private String firstname;
    private String lastname;
    private String companyName;
    private String address1;
    private String address2;
    private String zipCode;
    private String city;

    @ManyToOne
    private Country country;

    private String email;
    private String phone;
    private String mobile;

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;

        Contact contact = (Contact) o;

        if (getCustomerNo() != null ? !getCustomerNo().equals(contact.getCustomerNo()) : contact.getCustomerNo() != null)
            return false;
        if (getFirstname() != null ? !getFirstname().equals(contact.getFirstname()) : contact.getFirstname() != null)
            return false;
        if (getLastname() != null ? !getLastname().equals(contact.getLastname()) : contact.getLastname() != null)
            return false;
        if (getCompanyName() != null ? !getCompanyName().equals(contact.getCompanyName()) : contact.getCompanyName() != null)
            return false;
        if (getAddress1() != null ? !getAddress1().equals(contact.getAddress1()) : contact.getAddress1() != null)
            return false;
        if (getAddress2() != null ? !getAddress2().equals(contact.getAddress2()) : contact.getAddress2() != null)
            return false;
        if (getZipCode() != null ? !getZipCode().equals(contact.getZipCode()) : contact.getZipCode() != null)
            return false;
        if (getCity() != null ? !getCity().equals(contact.getCity()) : contact.getCity() != null) return false;
        if (getCountry() != null ? !getCountry().equals(contact.getCountry()) : contact.getCountry() != null)
            return false;
        if (getEmail() != null ? !getEmail().equals(contact.getEmail()) : contact.getEmail() != null) return false;
        if (getPhone() != null ? !getPhone().equals(contact.getPhone()) : contact.getPhone() != null) return false;
        return getMobile() != null ? getMobile().equals(contact.getMobile()) : contact.getMobile() == null;
    }

    @Override
    public int hashCode() {
        int result = getCustomerNo() != null ? getCustomerNo().hashCode() : 0;
        result = 31 * result + (getFirstname() != null ? getFirstname().hashCode() : 0);
        result = 31 * result + (getLastname() != null ? getLastname().hashCode() : 0);
        result = 31 * result + (getCompanyName() != null ? getCompanyName().hashCode() : 0);
        result = 31 * result + (getAddress1() != null ? getAddress1().hashCode() : 0);
        result = 31 * result + (getAddress2() != null ? getAddress2().hashCode() : 0);
        result = 31 * result + (getZipCode() != null ? getZipCode().hashCode() : 0);
        result = 31 * result + (getCity() != null ? getCity().hashCode() : 0);
        result = 31 * result + (getCountry() != null ? getCountry().hashCode() : 0);
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (getPhone() != null ? getPhone().hashCode() : 0);
        result = 31 * result + (getMobile() != null ? getMobile().hashCode() : 0);
        return result;
    }
}
