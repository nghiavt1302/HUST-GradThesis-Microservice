package com.nghiavt.productservice.cqrs.command;

import com.nghiavt.productservice.core.database.repository.ProductLookupRepository;
import com.nghiavt.productservice.core.events.ProductCreatedEvent;
import com.nghiavt.productservice.core.database.hibernatemapping.ProductLookupEntity;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
public class PrroductLookupEventHandler {

    private final ProductLookupRepository productLookupRepository;

    public PrroductLookupEventHandler(ProductLookupRepository productLookupRepository) {
        this.productLookupRepository = productLookupRepository;
    }

    @EventHandler
    public void on(ProductCreatedEvent event){
        ProductLookupEntity e = new ProductLookupEntity(event.getProductId(), event.getTitle());
        productLookupRepository.save(e);
    }
}
