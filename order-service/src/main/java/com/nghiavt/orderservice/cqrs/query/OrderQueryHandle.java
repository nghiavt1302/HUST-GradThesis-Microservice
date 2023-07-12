package com.nghiavt.orderservice.cqrs.query;

import com.nghiavt.orderservice.core.database.hibernatemapping.OrderEntity;
import com.nghiavt.orderservice.core.database.repository.OrderRepository;
import com.nghiavt.orderservice.core.model.OrderSummary;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderQueryHandle {
    @Autowired
    OrderRepository orderRepository;

    @QueryHandler
    public OrderSummary findOrder(FindOrderQuery findOrderQuery){
        OrderEntity orderEntity = orderRepository.findByOrderId(findOrderQuery.getOrderId());
        return new OrderSummary(orderEntity.getOrderId(), orderEntity.getOrderStatus(), "");
    }
}
