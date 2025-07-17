package com.sope.sope_ecommerce_backend.modules.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
@Data
public class WishlistId {
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "product_id")
    private UUID productId;
}
