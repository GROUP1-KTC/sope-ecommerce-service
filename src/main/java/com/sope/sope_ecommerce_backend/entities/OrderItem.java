package com.sope.sope_ecommerce_backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem {
    @EmbeddedId
    private OrderItemId orderItemId;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @MapsId("productVariantId")
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false, name = "commission_fee_percent", precision = 5, scale = 2)
    private BigDecimal commissionFeePercent;
}
