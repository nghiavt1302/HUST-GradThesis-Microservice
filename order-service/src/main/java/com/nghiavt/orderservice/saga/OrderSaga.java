package com.nghiavt.orderservice.saga;

import com.nghiavt.common.commands.ReverseProductCommand;
import com.nghiavt.orderservice.core.event.OrderCreatedEvent;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;


@Saga
public class OrderSaga {
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
        commandGateway.send(reverseProductCommand, new CommandCallback<ReverseProductCommand, Object>() {
            @Override
            public void onResult(@Nonnull CommandMessage<? extends ReverseProductCommand> commandMessage,
                                 @Nonnull CommandResultMessage<?> commandResultMessage) {
                if (commandResultMessage.isExceptional()){

                }
            }
        });
    }
}
