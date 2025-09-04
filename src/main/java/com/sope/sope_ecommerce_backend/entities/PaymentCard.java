package com.sope.sope_ecommerce_backend.entities;

import com.sope.sope_ecommerce_backend.enums.CardType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payment_cards")
public class PaymentCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // Tên chủ thẻ
    private String cardHolderName;

    private String last4Digits;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    private LocalDate expiryDate;

    // Token do cổng thanh toán cung cấp (Stripe, PayPal, MoMo, ...)
    private String token;

    private Boolean isDefault = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser appUser;
}
