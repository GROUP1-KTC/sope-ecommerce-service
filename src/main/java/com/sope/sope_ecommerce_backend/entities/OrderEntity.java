package com.sope.sope_ecommerce_backend.entities;

import com.sope.sope_ecommerce_backend.enums.OrderStatus;
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
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "order_id", columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private AddressEntity shippingAddress;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    private BigDecimal total;

    @Column(name = "shipping_charges")
    private BigDecimal shippingCharges;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    private String note;

    @Builder.Default
    private OrderStatus status =OrderStatus.PENDING;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItemEntity> orderItems;

    @ManyToOne
    @JoinColumn(name = "discount_code_id")
    private DiscountCodeEntity discountCode;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderStatusHistoryEntity> statusHistory;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private PaymentEntity payment;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CommissionEntity commission;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TransactionFeeEntity transactionFee;


}
