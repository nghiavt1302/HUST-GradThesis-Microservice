package com.nghiavt.productservice.cqrs.query;

import com.nghiavt.productservice.core.database.hibernatemapping.ProductEntity;
import com.nghiavt.productservice.core.database.repository.ProductRepository;
import com.nghiavt.productservice.core.model.ProductRestModel;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductQueryHanler {
    private final ProductRepository productRepository;

    public ProductQueryHanler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @QueryHandler
    public List<ProductRestModel> findProducts(FindProductsQuery q){
        List<ProductRestModel> res = new ArrayList<>();
        List<ProductEntity> entities = productRepository.findAll();
        for (ProductEntity p : entities) {
            res.add(ProductRestModel.builder()
                            .title(p.getTitle())
                            .price(p.getPrice())
                            .productId(p.getProductId())
                            .quantity(p.getQuantity())
                    .build());
        }
        return res;
    }
}
