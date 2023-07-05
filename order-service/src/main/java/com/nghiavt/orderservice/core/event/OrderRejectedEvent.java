package com.nghiavt.orderservice.core.event;

import com.nghiavt.orderservice.core.model.constants.OrderStatus;
import lombok.Value;

@Value
public class OrderRejectedEvent {
    private final String orderId;
    private final String reason;
    private final OrderStatus orderStatus = OrderStatus.REJECTED;

}
