package com.nghiavt.orderservice.cqrs.command.controller;

import com.nghiavt.orderservice.core.model.constants.OrderStatus;
import com.nghiavt.orderservice.core.model.CreateOrderModel;
import com.nghiavt.orderservice.cqrs.command.commandobject.CreateOrderCommand;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderCommandController {
    private final CommandGateway commandGateway;

    @Autowired
    public OrderCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String createOrder(@Valid @RequestBody CreateOrderModel order) {
        String userId = "nghiavt";
        CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
                .addressId(order.getAddressId())
                .productId(order.getProductId())
                .userId(userId)
                .quantity(order.getQuantity())
                .orderId(UUID.randomUUID().toString().substring(0,8))
                .orderStatus(OrderStatus.CREATED)
                .build();
        return commandGateway.sendAndWait(createOrderCommand);
    }
}
