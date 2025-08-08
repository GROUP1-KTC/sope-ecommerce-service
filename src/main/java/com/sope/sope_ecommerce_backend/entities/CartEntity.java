package com.sope.sope_ecommerce_backend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartEntity {

    @EmbeddedId
    CartId cartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")  // Liên kết với userId trong CartId
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("user-carts")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productVariantId")
    @JoinColumn(name = "product_variant_id", nullable = false)
    @JsonBackReference
    private ProductVariantEntity productVariant;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "added_at")
    private LocalDateTime addedAt;
}

