package com.nghiavt.paymentservice.core.event;

import com.nghiavt.common.events.PaymentProcessedEvent;
import com.nghiavt.paymentservice.core.database.entity.PaymentEntity;
import com.nghiavt.paymentservice.core.database.repository.PaymentRepository;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(PaymentEventHandler.class);
    private final PaymentRepository paymentRepository;
    @Autowired
    public PaymentEventHandler(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @EventHandler
    public void on(PaymentProcessedEvent event) {
        LOGGER.info("PaymentProcessedEvent is called for orderId: " + event.getOrderId());

        PaymentEntity paymentEntity = new PaymentEntity();
        BeanUtils.copyProperties(event, paymentEntity);

        paymentRepository.save(paymentEntity);

    }
}
