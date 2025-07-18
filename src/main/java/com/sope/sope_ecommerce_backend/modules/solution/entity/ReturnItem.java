package com.sope.sope_ecommerce_backend.modules.solution.entity;

import com.sope.sope_ecommerce_backend.modules.order.entity.OrderItem;
import com.sope.sope_ecommerce_backend.modules.product.entity.ProductVariant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "return_items")
public class ReturnItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "return_item_id", updatable = false, nullable = false)
    private UUID returnItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_request_id", nullable = false)
    private ReturnRequest returnRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    @Column(name = "reason_detail", columnDefinition = "TEXT")
    private String reasonDetail;

    @Column(name = "refund_amount_per_item", precision = 10, scale = 2, nullable = false)
    private BigDecimal refundAmountPerItem;

    @Column(nullable = false)
    private int quantity;
}
