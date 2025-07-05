package com.sope.sope_ecommerce_backend.modules.order.entity;

import com.sope.sope_ecommerce_backend.modules.order.enums.DiscountType;
import jakarta.persistence.*;
import lombok.*;
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
public class DiscountCode {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @Column(length = 5, unique = true, nullable = false)
    private String code;

    @Column
    private double discountValue;


    @Column
    private Double maxDiscountValue;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType;

    @Column
    private int maxUsage;

    @Column
    private int currentUsage = 0;

    @Column
    private LocalDateTime createdAt;


    @Column
    private LocalDateTime startDate; // Start of discount validity (e.g., School Day, Black Friday)

    @Column
    private LocalDateTime endDate; // End of discount validity

    @OneToMany(mappedBy = "discountCode", cascade = CascadeType.ALL)
    private List<Order> orders;


    @PrePersist
    @PreUpdate
    private void validateMaxDiscountValue() {
        if (discountType == DiscountType.PERCENTAGE && (maxDiscountValue == null || maxDiscountValue <= 0)) {
            throw new IllegalStateException("maxDiscountValue must be greater than 0 for PERCENTAGE discount type");
        }
    }
}


