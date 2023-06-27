package com.nghiavt.orderservice.saga;

import com.nghiavt.common.commands.ReverseProductCommand;
import com.nghiavt.common.events.ProductReservedEvent;
import com.nghiavt.orderservice.core.event.OrderCreatedEvent;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

@Saga
public class OrderSaga {
    private static final Logger LOG = LoggerFactory.getLogger((OrderSaga.class));
    @Autowired
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent) {
        ReverseProductCommand reverseProductCommand = ReverseProductCommand.builder()
                .orderId(orderCreatedEvent.getOrderId())
                .productId(orderCreatedEvent.getProductId())
                .userId(orderCreatedEvent.getProductId())
                .quantity(orderCreatedEvent.getQuantity())
                .build();

        LOG.info("Order created event handled, order ID: " + reverseProductCommand.getOrderId() +
                ", product ID: " + reverseProductCommand.getProductId());
        commandGateway.send(reverseProductCommand, new CommandCallback<ReverseProductCommand, Object>() {
            @Override
            public void onResult(@Nonnull CommandMessage<? extends ReverseProductCommand> commandMessage,
                                 @Nonnull CommandResultMessage<?> commandResultMessage) {
                if (commandResultMessage.isExceptional()){

                }
            }
        });
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent productReservedEvent){
        LOG.info("Product reserved event is called, order ID: " + productReservedEvent.getOrderId() +
                ", product ID: " + productReservedEvent.getProductId());
    }
}
