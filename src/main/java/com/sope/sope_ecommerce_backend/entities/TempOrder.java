package com.sope.sope_ecommerce_backend.entities;

import com.sope.sope_ecommerce_backend.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "temp_orders")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TempOrder {
    @Id
    @GeneratedValue
    private UUID id;

    private String idempotencyKey;
    private String orderNumber;

    private String guestEmail;
    private String guestName;
    private String guestPhone;

    @ManyToOne(fetch = FetchType.LAZY)
    private Shop shop;


    private String shippingAddress;
    private String city;
    private String district;
    private  String ward;

    @Column(nullable = false)
    private String shippingRateId;

    @ElementCollection
    @CollectionTable(name = "temp_order_items", joinColumns = @JoinColumn(name = "temp_order_id"))
    private List<TempOrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private BigDecimal subTotal;
    private BigDecimal shippingCharges;
    private BigDecimal totalAmount;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
}



