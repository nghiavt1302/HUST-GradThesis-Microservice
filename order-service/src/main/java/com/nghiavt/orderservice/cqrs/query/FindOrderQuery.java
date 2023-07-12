package com.nghiavt.orderservice.cqrs.query;

import lombok.Value;

@Value
public class FindOrderQuery {
    private final String orderId;
}
