package com.nghiavt.paymentservice.core.database.repository;

import com.nghiavt.paymentservice.core.database.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {
}
