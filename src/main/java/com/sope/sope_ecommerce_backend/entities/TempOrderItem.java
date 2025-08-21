package com.sope.sope_ecommerce_backend.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TempOrderItem {
    private UUID productVariantId;
    private int quantity;
    private BigDecimal price;
}