package com.nghiavt.orderservice.cqrs.command.commandobject;

import lombok.ToString;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
@ToString
public class RejectOrderCommand {
    @TargetAggregateIdentifier
    private final String orderId;
    private final String reason;
}
