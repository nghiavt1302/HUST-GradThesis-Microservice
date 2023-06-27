package com.nghiavt.productservice.core.database.repository;

import com.nghiavt.productservice.core.database.hibernatemapping.ProductLookupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLookupRepository extends JpaRepository<ProductLookupEntity, String> {
    ProductLookupEntity findByProductIdAndTitle(String productId, String title);
}
