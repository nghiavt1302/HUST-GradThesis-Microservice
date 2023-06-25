package com.nghiavt.productservice.core.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductModel {
    private String title;
    private BigDecimal price;
    private Integer quantity;
}
