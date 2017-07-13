package com.doit.wheels.dao.entities;

import com.doit.wheels.dao.entities.basic.AbstractModel;
import com.doit.wheels.utils.StatusTypeEnum;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order extends AbstractModel {

    private String orderNo;

    private Date created;

    private Date lastUpdated;

    @ManyToOne
    private User createdByUser;

    @ManyToOne
    private User lastUpdatedByUser;

    @Enumerated(EnumType.STRING)
    private StatusTypeEnum status = StatusTypeEnum.IN_CREATION;

    @ManyToOne
    private Customer customer;

    private String customerNumberOrder;

    private Date deadlineFinish;

    private Date deadlineDelivery;

    @Column(length = 1000)
    private String comment;

    @ManyToOne
    private User driver;

    @Lob
    private byte[] wheelsRimPicture;

    @Lob
    private byte[] signaturePicture;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "orders_wheel_rim_positions",joinColumns = { @JoinColumn(name = "order_id") }, inverseJoinColumns = { @JoinColumn(name = "wheel_rim_position_id") })
    private Set<WheelRimPosition> wheelRimPositions;

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "orders_guidelines",
            joinColumns = {@JoinColumn(name = "order_id")},
            inverseJoinColumns = {@JoinColumn(name = "guideline_id")})
    private Set<Guideline> guidelines = new HashSet<>();

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public User getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = createdByUser;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getCustomerNumberOrder() {
        return customerNumberOrder;
    }

    public void setCustomerNumberOrder(String customerNumberOrder) {
        this.customerNumberOrder = customerNumberOrder;
    }

    public Date getDeadlineFinish() {
        return deadlineFinish;
    }

    public void setDeadlineFinish(Date deadlineFinish) {
        this.deadlineFinish = deadlineFinish;
    }

    public Date getDeadlineDelivery() {
        return deadlineDelivery;
    }

    public void setDeadlineDelivery(Date deadlineDelivery) {
        this.deadlineDelivery = deadlineDelivery;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public byte[] getWheelsRimPicture() {
        return wheelsRimPicture;
    }

    public void setWheelsRimPicture(byte[] wheelsRimPicture) {
        this.wheelsRimPicture = wheelsRimPicture;
    }

    public byte[] getSignaturePicture() {
        return signaturePicture;
    }

    public void setSignaturePicture(byte[] signaturePicture) {
        this.signaturePicture = signaturePicture;
    }

    public User getLastUpdatedByUser() {
        return lastUpdatedByUser;
    }

    public void setLastUpdatedByUser(User lastUpdatedByUser) {
        this.lastUpdatedByUser = lastUpdatedByUser;
    }

    public StatusTypeEnum getStatus() {
        return status;
    }

    public void setStatus(StatusTypeEnum status) {
        this.status = status;
    }

    public Set<WheelRimPosition> getWheelRimPositions() {
        return wheelRimPositions;
    }

    public void setWheelRimPositions(Set<WheelRimPosition> wheelRimPositions) {
        this.wheelRimPositions = wheelRimPositions;
    }

    public Set<Guideline> getGuidelines() {
        return guidelines;
    }

    public void setGuidelines(Set<Guideline> guidelines) {
        this.guidelines = guidelines;
    }
}
