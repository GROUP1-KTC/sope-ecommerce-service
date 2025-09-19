package com.sope.sope_ecommerce_backend.entities;

import lombok.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TempOrderItem {
    @ManyToOne
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    private int quantity;
    private BigDecimal price;

    @Column(name = "commission_fee_percent", precision = 5, scale = 2)
    private BigDecimal commissionFeePercent;
}