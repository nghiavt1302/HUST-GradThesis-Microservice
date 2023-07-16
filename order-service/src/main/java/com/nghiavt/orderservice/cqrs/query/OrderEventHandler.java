package com.nghiavt.orderservice.cqrs.query;

import com.nghiavt.orderservice.core.database.hibernatemapping.OrderEntity;
import com.nghiavt.orderservice.core.database.repository.OrderRepository;
import com.nghiavt.orderservice.core.event.OrderApprovedEvent;
import com.nghiavt.orderservice.core.event.OrderCreatedEvent;
import com.nghiavt.orderservice.core.event.OrderRejectedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("order-group")
@Slf4j
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
        log.info("New order inserted to DB: " + orderEntity.toString());
    }

    @EventHandler
    public void on(OrderApprovedEvent event){
        OrderEntity orderEntity = orderRepository.findByOrderId(event.getOrderId());
        if (orderEntity == null){
            //
            return;
        }
        orderEntity.setOrderStatus(event.getOrderStatus());
        orderRepository.save(orderEntity);
    }

    @EventHandler
    public void on(OrderRejectedEvent event){
        OrderEntity orderEntity = orderRepository.findByOrderId(event.getOrderId());
        orderEntity.setOrderStatus(event.getOrderStatus());
        orderRepository.save(orderEntity);
    }
}
