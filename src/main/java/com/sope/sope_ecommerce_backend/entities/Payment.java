package com.sope.sope_ecommerce_backend.entities;

import com.sope.sope_ecommerce_backend.enums.PaymentProvider;
import com.sope.sope_ecommerce_backend.enums.PaymentStatus;
import com.sope.sope_ecommerce_backend.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "payment_id", columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID paymentId;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;


    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TempOrder> tempOrders;

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

    @Enumerated(EnumType.STRING)
    private PaymentProvider provider; // "MOMO", "VNPAY", "STRIPE"
    private String providerPaymentId; // id by provider return
    private String providerPayUrl; // redirect/qr url

    @Column(unique = true)
    private String requestId;


    public void addOrder(Order order) {
        orders.add(order);
        order.setPayment(this);
    }

    public void addTempOrder(TempOrder tempOrder) {
        tempOrders.add(tempOrder);
        tempOrder.setPayment(this);
    }

    public void setTempOrders(List<TempOrder> tempOrders) {
        this.tempOrders = tempOrders;
        if (tempOrders != null) {
            tempOrders.forEach(order -> order.setPayment(this));
        }
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        if (orders != null) {
            orders.forEach(order -> order.setPayment(this));
        }
    }


    @PrePersist
    @PreUpdate
    private void validateAssociation() {
        boolean hasOrders = orders != null && !orders.isEmpty();
        boolean hasTempOrders = tempOrders != null && !tempOrders.isEmpty();

        if (!hasOrders && !hasTempOrders) {
            throw new IllegalStateException("Payment must be linked to either an Order or a TempOrder");
        }
        if (hasOrders && hasTempOrders) {
            throw new IllegalStateException("Payment cannot be linked to both Order and TempOrder at the same time");
        }
    }

}
