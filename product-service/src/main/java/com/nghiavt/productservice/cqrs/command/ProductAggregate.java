package com.nghiavt.productservice.cqrs.command;

import com.nghiavt.productservice.core.events.ProductCreatedEvent;
import com.nghiavt.productservice.cqrs.command.commandobject.CreateProductCommand;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;

@Aggregate
public class ProductAggregate {
    @AggregateIdentifier
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;
    public ProductAggregate(){
    }
    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand){
        // Validate create product command
        if (createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Price must be greater than 0.");
        }
        if(createProductCommand.getTitle() == null ||
                createProductCommand.getTitle().isBlank()){
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        productId = createProductCommand.getProductId();

        ProductCreatedEvent productCreatedEvent = ProductCreatedEvent.builder()
                .productId(productId)
                .quantity(createProductCommand.getQuantity())
                .price(createProductCommand.getPrice())
                .title(createProductCommand.getTitle())
                .build();
//        BeanUtils.copyProperties(createProductCommand, productCreatedEvent);
        AggregateLifecycle.apply(productCreatedEvent);
//        if (true){
//            throw new Exception("Something goes wrong");
//        }
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent productCreatedEvent){
        this.productId = productCreatedEvent.getProductId();
        this.price = productCreatedEvent.getPrice();
        this.quantity = productCreatedEvent.getQuantity();
        this.title = productCreatedEvent.getTitle();
    }
}
