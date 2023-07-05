package com.nghiavt.orderservice.core.event;

import com.nghiavt.orderservice.core.model.constants.OrderStatus;
import lombok.Value;

@Value
public class OrderApprovedEvent {
    private final String orderId;
    private final OrderStatus orderStatus = OrderStatus.APPROVED;
}
