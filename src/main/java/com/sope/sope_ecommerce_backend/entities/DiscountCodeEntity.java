package com.sope.sope_ecommerce_backend.entities;

import com.sope.sope_ecommerce_backend.enums.DiscountType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.GenericGenerator;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "discount_codes")
public class DiscountCodeEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "discount_code_id", columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID discountCodeId;

    @Column(length = 5, unique = true, nullable = false)
    private String code;

    @Column(name = "discount_value", nullable = false)
    private BigDecimal discountValue;

    @Column(name = "max_discount_value", nullable = false)
    private BigDecimal maxDiscountValue;

    @Column(name = "min_order_value", nullable = false)
    private BigDecimal minOrderValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType;

    @Column(name = "max_usage")
    private int maxUsage;

    @Column(name = "current_usage")
    @Builder.Default
    private int currentUsage = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate; // End of discount validity

    @OneToMany(mappedBy = "discountCode", cascade = CascadeType.ALL)
    private List<OrderEntity> orders;

    @OneToMany(mappedBy = "discountCode", cascade = CascadeType.ALL)
    private List<DiscountCodeScopeEntity> scopes;

    @PrePersist
    @PreUpdate
    private void validateDiscountCode() {
        if (discountType == DiscountType.PERCENTAGE) {
            if (maxDiscountValue == null || maxDiscountValue.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalStateException("maxDiscountValue must be greater than 0 for PERCENTAGE discount type");
            }
            // Thêm kiểm tra discountValue cho PERCENTAGE (0-100)
            if (discountValue.compareTo(BigDecimal.ZERO) <= 0 || discountValue.compareTo(new BigDecimal("100")) > 0) {
                throw new IllegalStateException("discountValue for PERCENTAGE must be between 0 and 100");
            }
        } else if (discountType == DiscountType.FIXED_AMOUNT) {
            if (discountValue.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalStateException("discountValue for FIXED_AMOUNT must be greater than 0");
            }
        }


        // Validate start and end dates
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalStateException("endDate must be after startDate");
        }

        if (minOrderValue != null && minOrderValue.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("minOrderValue must be >= 0");
        }

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}


