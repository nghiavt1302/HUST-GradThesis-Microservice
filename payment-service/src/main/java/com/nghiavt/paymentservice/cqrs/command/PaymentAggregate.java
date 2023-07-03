package com.nghiavt.paymentservice.cqrs.command;

import com.nghiavt.common.commands.ProcessPaymentCommand;
import com.nghiavt.common.events.PaymentProcessedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class PaymentAggregate {
    @AggregateIdentifier
    private String paymentId;

    private String orderId;

    public PaymentAggregate() { }

    @CommandHandler
    public PaymentAggregate(ProcessPaymentCommand processPaymentCommand){

        if(processPaymentCommand.getPaymentDetail() == null) {
            throw new IllegalArgumentException("Missing payment details");
        }

        if(processPaymentCommand.getOrderId() == null) {
            throw new IllegalArgumentException("Missing orderId");
        }

        if(processPaymentCommand.getPaymentId() == null) {
            throw new IllegalArgumentException("Missing paymentId");
        }

        AggregateLifecycle.apply(PaymentProcessedEvent.builder()
                .orderId(processPaymentCommand.getOrderId())
                .paymentId(processPaymentCommand.getPaymentId())
                .build());
    }

    @EventSourcingHandler
    protected void on(PaymentProcessedEvent paymentProcessedEvent){
        this.paymentId = paymentProcessedEvent.getPaymentId();
        this.orderId = paymentProcessedEvent.getOrderId();
    }
}
