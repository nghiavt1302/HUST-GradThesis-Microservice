package com.nghiavt.orderservice.core.database.repository;

import com.nghiavt.orderservice.core.database.hibernatemapping.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {
}
