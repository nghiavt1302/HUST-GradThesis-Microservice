package com.nghiavt.productservice.core.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class CreateProductModel {
    @NotBlank
    private String title;
    @Min(value = 1, message = "Price must be higher than 1.")
    private BigDecimal price;
    @Min(value = 1, message = "Quantity must be higher than 1.")
    @Max(value = 5, message = "Quantity must be lower than 5.")
    private Integer quantity;
}
