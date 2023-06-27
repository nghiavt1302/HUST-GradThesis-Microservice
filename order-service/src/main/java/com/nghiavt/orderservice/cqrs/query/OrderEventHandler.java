package com.nghiavt.orderservice.cqrs.query;

import com.nghiavt.orderservice.core.database.hibernatemapping.OrderEntity;
import com.nghiavt.orderservice.core.database.repository.OrderRepository;
import com.nghiavt.orderservice.core.event.OrderCreatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("order-group")
public class OrderEventHandler {
    private final OrderRepository orderRepository;

    public OrderEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventHandler
    public void on(OrderCreatedEvent event) throws Exception {
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(event, orderEntity);

        this.orderRepository.save(orderEntity);
    }
}
