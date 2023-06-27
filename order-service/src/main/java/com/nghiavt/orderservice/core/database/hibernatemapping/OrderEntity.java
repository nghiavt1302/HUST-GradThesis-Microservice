package com.nghiavt.orderservice.core.database.hibernatemapping;

import com.nghiavt.orderservice.core.model.constants.OrderStatus;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "orders")
@Data
public class OrderEntity implements Serializable {
    private static final long serialVersionUID = -9138291618445822231L;
    @Id
    @Column(unique = true)
    public String orderId;
    private String productId;
    private String userId;
    private int quantity;
    private String addressId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
