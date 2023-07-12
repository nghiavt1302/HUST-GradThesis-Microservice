package com.nghiavt.orderservice.core.model;

import com.nghiavt.orderservice.core.model.constants.OrderStatus;
import lombok.Value;

@Value
public class OrderSummary {
    private final String orderId;
    private final OrderStatus orderStatus;
    private final String message;

}
