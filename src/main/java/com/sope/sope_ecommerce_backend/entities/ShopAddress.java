package com.sope.sope_ecommerce_backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "shop_addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String senderName;

    @Column(nullable = false)
    private String senderPhone;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String ward;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    @JsonIgnore
    private Shop shop;
}
