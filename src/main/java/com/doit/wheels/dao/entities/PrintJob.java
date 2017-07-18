package com.doit.wheels.dao.entities;

import com.doit.wheels.dao.entities.basic.AbstractModel;
import com.doit.wheels.utils.enums.PrintJobStatusEnum;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class PrintJob extends AbstractModel{

    @ManyToOne
    private Order order;

    @Enumerated(EnumType.STRING)
    private PrintJobStatusEnum printJobStatusEnum;

    private Date jobCreated;

    @ManyToOne
    private User user;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public PrintJobStatusEnum getPrintJobStatusEnum() {
        return printJobStatusEnum;
    }

    public void setPrintJobStatusEnum(PrintJobStatusEnum printJobStatusEnum) {
        this.printJobStatusEnum = printJobStatusEnum;
    }

    public Date getJobCreated() {
        return jobCreated;
    }

    public void setJobCreated(Date jobCreated) {
        this.jobCreated = jobCreated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
