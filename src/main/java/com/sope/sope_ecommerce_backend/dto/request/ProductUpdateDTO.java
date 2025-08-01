package com.sope.sope_ecommerce_backend.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductUpdateDTO {
    private String name;
    private BigDecimal defaultPrice;
    private String brand;
    private String description;
    private UUID categoryId;
    private Integer stock;
    private Boolean hidden;
}