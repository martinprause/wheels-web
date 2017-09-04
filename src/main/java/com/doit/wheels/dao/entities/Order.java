package com.doit.wheels.dao.entities;

import com.doit.wheels.dao.entities.basic.AbstractModel;
import com.doit.wheels.utils.enums.StatusTypeEnum;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "orders")
public class Order extends AbstractModel implements Cloneable{

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
    private byte[] wheelsRimPicture1;

    @Lob
    private byte[] wheelsRimPicture2;

    @Lob
    private byte[] wheelsRimPicture3;

    @Lob
    private byte[] wheelsRimPicture4;

    private String signatureName;

    @Lob
    private byte[] signaturePicture;

    private String qrCode;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "orders_wheel_rim_positions",joinColumns = { @JoinColumn(name = "order_id") }, inverseJoinColumns = { @JoinColumn(name = "wheel_rim_position_id") })
    private Set<WheelRimPosition> wheelRimPositions;

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.EAGER)
    @JoinTable(name = "orders_guidelines",
            joinColumns = {@JoinColumn(name = "order_id")},
            inverseJoinColumns = {@JoinColumn(name = "guideline_id")})
    private Set<Guideline> guidelines = new HashSet<>();

    @OneToMany(mappedBy = "order")
    private List<PrintJob> printJobs;

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

    public List<PrintJob> getPrintJobs() {
        return printJobs;
    }

    public void setPrintJobs(List<PrintJob> printJobs) {
        this.printJobs = printJobs;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public byte[] getWheelsRimPicture1() {
        return wheelsRimPicture1;
    }

    public void setWheelsRimPicture1(byte[] wheelsRimPicture1) {
        this.wheelsRimPicture1 = wheelsRimPicture1;
    }

    public byte[] getWheelsRimPicture2() {
        return wheelsRimPicture2;
    }

    public void setWheelsRimPicture2(byte[] wheelsRimPicture2) {
        this.wheelsRimPicture2 = wheelsRimPicture2;
    }

    public byte[] getWheelsRimPicture3() {
        return wheelsRimPicture3;
    }

    public void setWheelsRimPicture3(byte[] wheelsRimPicture3) {
        this.wheelsRimPicture3 = wheelsRimPicture3;
    }

    public byte[] getWheelsRimPicture4() {
        return wheelsRimPicture4;
    }

    public void setWheelsRimPicture4(byte[] wheelsRimPicture4) {
        this.wheelsRimPicture4 = wheelsRimPicture4;
    }

    public String getSignatureName() {
        return signatureName;
    }

    public void setSignatureName(String signatureName) {
        this.signatureName = signatureName;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (orderNo != null ? !orderNo.equals(order.orderNo) : order.orderNo != null) return false;
        if (created != null ? !created.equals(order.created) : order.created != null) return false;
        if (lastUpdated != null ? !lastUpdated.equals(order.lastUpdated) : order.lastUpdated != null) return false;
        if (createdByUser != null ? !createdByUser.equals(order.createdByUser) : order.createdByUser != null)
            return false;
        if (lastUpdatedByUser != null ? !lastUpdatedByUser.equals(order.lastUpdatedByUser) : order.lastUpdatedByUser != null)
            return false;
        if (status != order.status) return false;
        if (customer != null ? !customer.equals(order.customer) : order.customer != null) return false;
        if (customerNumberOrder != null ? !customerNumberOrder.equals(order.customerNumberOrder) : order.customerNumberOrder != null)
            return false;
        if (deadlineFinish != null ? !deadlineFinish.equals(order.deadlineFinish) : order.deadlineFinish != null)
            return false;
        if (deadlineDelivery != null ? !deadlineDelivery.equals(order.deadlineDelivery) : order.deadlineDelivery != null)
            return false;
        if (comment != null ? !comment.equals(order.comment) : order.comment != null) return false;
        if (driver != null ? !driver.equals(order.driver) : order.driver != null) return false;
        if (!Arrays.equals(wheelsRimPicture1, order.wheelsRimPicture1)) return false;
        if (!Arrays.equals(wheelsRimPicture2, order.wheelsRimPicture2)) return false;
        if (!Arrays.equals(wheelsRimPicture3, order.wheelsRimPicture3)) return false;
        if (!Arrays.equals(wheelsRimPicture4, order.wheelsRimPicture4)) return false;
        if (signatureName != null ? !signatureName.equals(order.signatureName) : order.signatureName != null)
            return false;
        if (!Arrays.equals(signaturePicture, order.signaturePicture)) return false;
        if (qrCode != null ? !qrCode.equals(order.qrCode) : order.qrCode != null) return false;
        if (wheelRimPositions != null ? !wheelRimPositions.equals(order.wheelRimPositions) : order.wheelRimPositions != null)
            return false;
        if (guidelines != null ? !guidelines.equals(order.guidelines) : order.guidelines != null) return false;
        return printJobs != null ? printJobs.equals(order.printJobs) : order.printJobs == null;
    }

    @Override
    public int hashCode() {
        int result = orderNo != null ? orderNo.hashCode() : 0;
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (lastUpdated != null ? lastUpdated.hashCode() : 0);
        result = 31 * result + (createdByUser != null ? createdByUser.hashCode() : 0);
        result = 31 * result + (lastUpdatedByUser != null ? lastUpdatedByUser.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        result = 31 * result + (customerNumberOrder != null ? customerNumberOrder.hashCode() : 0);
        result = 31 * result + (deadlineFinish != null ? deadlineFinish.hashCode() : 0);
        result = 31 * result + (deadlineDelivery != null ? deadlineDelivery.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (driver != null ? driver.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(wheelsRimPicture1);
        result = 31 * result + Arrays.hashCode(wheelsRimPicture2);
        result = 31 * result + Arrays.hashCode(wheelsRimPicture3);
        result = 31 * result + Arrays.hashCode(wheelsRimPicture4);
        result = 31 * result + (signatureName != null ? signatureName.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(signaturePicture);
        result = 31 * result + (qrCode != null ? qrCode.hashCode() : 0);
        result = 31 * result + (wheelRimPositions != null ? wheelRimPositions.hashCode() : 0);
        result = 31 * result + (guidelines != null ? guidelines.hashCode() : 0);
        result = 31 * result + (printJobs != null ? printJobs.hashCode() : 0);
        return result;
    }
}
