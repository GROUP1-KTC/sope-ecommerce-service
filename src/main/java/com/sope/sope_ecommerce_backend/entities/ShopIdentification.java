package com.sope.sope_ecommerce_backend.entities;

import com.sope.sope_ecommerce_backend.enums.IdentifierType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "shop_identifications")
public class ShopIdentification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IdentifierType idType;

    @Column(nullable = false)
    private String idName;

    @Column(nullable = false)
    private String idNumber;

    private String idFront;
    private String idBack;
    private String selfie;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false, unique = true)
    private Shop shop;
}
