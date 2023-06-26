package com.nghiavt.productservice.cqrs.query;

import com.nghiavt.productservice.core.database.hibernatemapping.ProductEntity;
import com.nghiavt.productservice.core.database.repository.ProductRepository;
import com.nghiavt.productservice.core.events.ProductCreatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
public class ProductEventHandler {
    private final ProductRepository productRepository;

    @Autowired
    public ProductEventHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @EventHandler
    public void on(ProductCreatedEvent event){
        productRepository.save(ProductEntity.builder()
                        .productId(event.getProductId())
                        .title(event.getTitle())
                        .price(event.getPrice())
                        .quantity(event.getQuantity())
                .build());
    }
}
