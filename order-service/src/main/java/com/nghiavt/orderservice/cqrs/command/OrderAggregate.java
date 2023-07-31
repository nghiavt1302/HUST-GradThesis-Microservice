package com.nghiavt.orderservice.cqrs.command;

import com.nghiavt.orderservice.core.event.OrderApprovedEvent;
import com.nghiavt.orderservice.core.event.OrderRejectedEvent;
import com.nghiavt.orderservice.core.model.constants.OrderStatus;
import com.nghiavt.orderservice.core.event.OrderCreatedEvent;
import com.nghiavt.orderservice.cqrs.command.commandobject.ApproveOrderCommand;
import com.nghiavt.orderservice.cqrs.command.commandobject.CreateOrderCommand;
import com.nghiavt.orderservice.cqrs.command.commandobject.RejectOrderCommand;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Slf4j
public class OrderAggregate {
    @AggregateIdentifier
    private String orderId;
    private String productId;
    private String userId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;

    public OrderAggregate() {
    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand) {
        OrderCreatedEvent orderCreatedEvent = OrderCreatedEvent.builder()
                .orderId(createOrderCommand.orderId)
                .userId(createOrderCommand.getUserId())
                .addressId(createOrderCommand.getAddressId())
                .productId(createOrderCommand.getProductId())
                .orderStatus(OrderStatus.CREATED)
                .quantity(createOrderCommand.getQuantity()).build();
        AggregateLifecycle.apply(orderCreatedEvent);
        log.info("Published Order created event " + orderCreatedEvent.toString());
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent orderCreatedEvent) throws Exception {
        this.orderId = orderCreatedEvent.getOrderId();
        this.productId = orderCreatedEvent.getProductId();
        this.userId = orderCreatedEvent.getUserId();
        this.addressId = orderCreatedEvent.getAddressId();
        this.quantity = orderCreatedEvent.getQuantity();
        this.orderStatus = orderCreatedEvent.getOrderStatus();
    }

    @CommandHandler
    public void handle(ApproveOrderCommand approveOrderCommand){
        OrderApprovedEvent orderApprovedEvent = new OrderApprovedEvent(approveOrderCommand.getOrderId());
        AggregateLifecycle.apply(orderApprovedEvent);
    }

    @EventSourcingHandler
    protected void on(OrderApprovedEvent orderApprovedEvent){
        this.orderStatus = orderApprovedEvent.getOrderStatus();
    }

    @CommandHandler
    public void handle(RejectOrderCommand command){
        OrderRejectedEvent event = new OrderRejectedEvent(command.getOrderId(), command.getReason());
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    protected void on(OrderRejectedEvent event){
        this.orderStatus = event.getOrderStatus();
    }
}
