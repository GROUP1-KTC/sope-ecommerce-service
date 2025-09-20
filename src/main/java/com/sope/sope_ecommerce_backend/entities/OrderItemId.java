package com.sope.sope_ecommerce_backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemId {
    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "product_variant_id")
    private UUID productVariantId;
}
