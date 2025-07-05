package com.sope.sope_ecommerce_backend.modules.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_variants")
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String image;

    @Column
    private String color;

    @Column
    private String size;

    @Column(nullable = false)
    private double price;

    @Column
    private int stock;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
