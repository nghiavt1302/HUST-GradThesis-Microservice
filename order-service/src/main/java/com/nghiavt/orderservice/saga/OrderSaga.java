package com.nghiavt.orderservice.saga;

import com.nghiavt.common.commands.CancelProductReservationCommand;
import com.nghiavt.common.commands.ProcessPaymentCommand;
import com.nghiavt.common.commands.ReverseProductCommand;
import com.nghiavt.common.events.PaymentProcessedEvent;
import com.nghiavt.common.events.ProductReservationCancelledEvent;
import com.nghiavt.common.events.ProductReservedEvent;
import com.nghiavt.common.model.User;
import com.nghiavt.common.query.FetchUserPaymentDetailQuery;
import com.nghiavt.orderservice.core.event.OrderApprovedEvent;
import com.nghiavt.orderservice.core.event.OrderCreatedEvent;
import com.nghiavt.orderservice.core.event.OrderRejectedEvent;
import com.nghiavt.orderservice.core.model.OrderSummary;
import com.nghiavt.orderservice.cqrs.command.commandobject.ApproveOrderCommand;
import com.nghiavt.orderservice.cqrs.command.commandobject.RejectOrderCommand;
import com.nghiavt.orderservice.cqrs.query.FindOrderQuery;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.annotation.DeadlineHandler;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Saga
public class OrderSaga {
    private static final Logger LOG = LoggerFactory.getLogger((OrderSaga.class));
    @Autowired
    private transient CommandGateway commandGateway;
    @Autowired
    private transient QueryGateway queryGateway;
    @Autowired
    private transient DeadlineManager deadlineManager;
    @Autowired
    private transient QueryUpdateEmitter queryUpdateEmitter;

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
        String userId = productReservedEvent.getUserId();
        FetchUserPaymentDetailQuery query = new FetchUserPaymentDetailQuery(userId);
        User userWithPaymentDetail = null;
        try {
            userWithPaymentDetail = queryGateway.query(query, ResponseTypes.instanceOf(User.class)).join();
        } catch (Exception ex){
            LOG.error(ex.getMessage());
            cancelProductReserve(productReservedEvent, ex.getMessage());
            // Compensating transaction
            return;
        }
        if (userWithPaymentDetail == null){
            // Compensating transaction
            cancelProductReserve(productReservedEvent, "Could not fetch user payment details.");
            return;
        }
        LOG.info("Fetched user payment detail, username: {} {}",
                userWithPaymentDetail.getFirstName(), userWithPaymentDetail.getLastName());

        deadlineManager.schedule(Duration.of(120, ChronoUnit.SECONDS),
                "payment-processing-deadline", productReservedEvent);

//        if (true) return; //for test deadline

        ProcessPaymentCommand processPaymentCommand = ProcessPaymentCommand.builder()
                .orderId(productReservedEvent.getOrderId())
                .paymentDetail(userWithPaymentDetail.getPaymentDetail())
                .paymentId(UUID.randomUUID().toString())
                .build();

        String res = null;
        try {
            res = commandGateway.sendAndWait(processPaymentCommand);
        } catch (Exception ex){
            LOG.error(ex.getMessage());
            // Compensating
            cancelProductReserve(productReservedEvent, ex.getMessage());
            return;
        }
        if (res == null){
            LOG.info("Process payment command resulted in Null. Compensating transaction");
            // Compensating
            cancelProductReserve(productReservedEvent, "Process payment command resulted in Null");
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentProcessedEvent paymentProcessedEvent){

        deadlineManager.cancelAll("payment-processing-deadline");

        ApproveOrderCommand approveOrderCommand =
                new ApproveOrderCommand(paymentProcessedEvent.getOrderId());
        commandGateway.send(approveOrderCommand);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderApprovedEvent orderApprovedEvent){
        LOG.info("Order is approved, order ID: {}", orderApprovedEvent.getOrderId());
        LOG.info("Saga is completed, order ID: {}", orderApprovedEvent.getOrderId());
        queryUpdateEmitter.emit(FindOrderQuery.class,
                query -> true,
                new OrderSummary(orderApprovedEvent.getOrderId(),
                        orderApprovedEvent.getOrderStatus(),""));
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservationCancelledEvent event){
        RejectOrderCommand command = new RejectOrderCommand(event.getOrderId(), event.getReason());
        commandGateway.send(command);
    }

    private void cancelProductReserve(ProductReservedEvent productReservedEvent, String reason){
        CancelProductReservationCommand cancel = CancelProductReservationCommand.builder()
                .orderId(productReservedEvent.getOrderId())
                .productId(productReservedEvent.getProductId())
                .quantity(productReservedEvent.getQuantity())
                .userId(productReservedEvent.getUserId())
                .reason(reason)
                .build();
        commandGateway.send(cancel);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderRejectedEvent event){
        LOG.info("Rejected order: {}", event.getOrderId());
        queryUpdateEmitter.emit(FindOrderQuery.class,
                query -> true,
                new OrderSummary(event.getOrderId(), event.getOrderStatus(), event.getReason()));
    }

    @DeadlineHandler(deadlineName = "payment-processing-deadline")
    public void handlePaymentDeadline(ProductReservedEvent productReservedEvent){
        LOG.info("Payment processing deadline triggered......................");
        cancelProductReserve(productReservedEvent, "timeout");
    }
}
