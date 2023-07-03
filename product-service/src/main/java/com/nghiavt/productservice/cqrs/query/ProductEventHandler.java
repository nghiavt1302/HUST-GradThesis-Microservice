package com.nghiavt.productservice.cqrs.query;

import com.nghiavt.common.events.ProductReservedEvent;
import com.nghiavt.productservice.core.database.hibernatemapping.ProductEntity;
import com.nghiavt.productservice.core.database.repository.ProductRepository;
import com.nghiavt.productservice.core.events.ProductCreatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
public class ProductEventHandler {
    private static final Logger LOG = LoggerFactory.getLogger((ProductEventHandler.class));
    private final ProductRepository productRepository;

    @Autowired
    public ProductEventHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @ExceptionHandler(resultType = Exception.class)
    public void handle(Exception e) throws Exception {
        throw e;
    }

    @ExceptionHandler(resultType = IllegalStateException.class)
    public void handle(IllegalStateException e){

    }

    @EventHandler
    public void on(ProductCreatedEvent event) throws Exception {
        productRepository.save(ProductEntity.builder()
                        .productId(event.getProductId())
                        .title(event.getTitle())
                        .price(event.getPrice())
                        .quantity(event.getQuantity())
                .build());
    }

    @EventHandler
    public void on(ProductReservedEvent productReservedEvent){
        ProductEntity productEntity= productRepository.findByProductId(productReservedEvent.getProductId());
        productEntity.setQuantity(productEntity.getQuantity() - productReservedEvent.getQuantity());
        productRepository.save(productEntity);

        LOG.info("Product reserved event is called, product ID: " + productReservedEvent.getProductId()
                + ", order ID: " + productReservedEvent.getOrderId());
    }
}
