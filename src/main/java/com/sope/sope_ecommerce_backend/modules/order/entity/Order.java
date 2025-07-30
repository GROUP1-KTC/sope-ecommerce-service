package com.sope.sope_ecommerce_backend.modules.order.entity;

import com.sope.sope_ecommerce_backend.modules.discount.entity.DiscountCode;
import com.sope.sope_ecommerce_backend.modules.order.enums.OrderStatus;
import com.sope.sope_ecommerce_backend.modules.payment.entity.Payment;
import com.sope.sope_ecommerce_backend.modules.revenue.entity.Commission;
import com.sope.sope_ecommerce_backend.modules.revenue.entity.TransactionFee;
import com.sope.sope_ecommerce_backend.modules.address.entity.AddressEntity;
import com.sope.sope_ecommerce_backend.modules.user.entity.User;
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
public class Order {
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
    private List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "discount_code_id")
    private DiscountCode discountCode;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderStatusHistory> statusHistory;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Commission commission;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TransactionFee transactionFee;


}
