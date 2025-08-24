package com.sope.sope_ecommerce_backend.entities;

import com.sope.sope_ecommerce_backend.enums.PaymentProvider;
import com.sope.sope_ecommerce_backend.enums.PaymentStatus;
import com.sope.sope_ecommerce_backend.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "payment_id", columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID paymentId;

    @OneToOne
    @JoinColumn(name = "order_id", unique = true)
    private Order order;

    @OneToOne
    @JoinColumn(name = "temp_order_id",  unique = true)
    private TempOrder tempOrder;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "payment_time", nullable = false)
    @CreationTimestamp
    private LocalDateTime paymentTime;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;

    private PaymentProvider provider; // "MOMO", "VNPAY", "STRIPE"
    private String providerPaymentId; // id by provider return
    private String providerPayUrl; // redirect/qr url

    @Column(unique = true)
    private String requestId;


    @PrePersist
    @PreUpdate
    private void validateAssociation() {
        if (order == null && tempOrder == null) {
            throw new IllegalStateException("Payment must be linked to either an Order or a TempOrder");
        }
        if (order != null && tempOrder != null) {
            throw new IllegalStateException("Payment cannot be linked to both Order and TempOrder at the same time");
        }
    }

}
