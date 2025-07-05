package com.sope.sope_ecommerce_backend.modules.order.entity;

import com.sope.sope_ecommerce_backend.modules.user.entity.Address;
import com.sope.sope_ecommerce_backend.modules.user.entity.User;
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
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address shippingAddress;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column
    private double totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "discount_code_id")
    private DiscountCode discountCode;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderStatusHistory> statusHistory;

    public enum OrderStatus {
        PENDING, CONFIRMED, SHIPPING, DELIVERED
    }
}
