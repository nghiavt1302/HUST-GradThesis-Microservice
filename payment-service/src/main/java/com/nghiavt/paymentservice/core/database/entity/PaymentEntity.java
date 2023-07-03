package com.nghiavt.paymentservice.core.database.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "payment")
public class PaymentEntity implements Serializable {
    private static final long serialVersionUID = 4421093995608832787L;
    @Id
    private String paymentId;
    @Column
    public String orderId;
}
