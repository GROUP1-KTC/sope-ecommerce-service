package com.sope.sope_ecommerce_backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_variants")
public class ProductVariantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_variant_id", columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID productVariantId;

    @Column(nullable = false)
    private BigDecimal price;

    private int stock;

    private int sold;

    private String imageVariant;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @ManyToMany(cascade = { CascadeType.PERSIST })
    @JoinTable(name = "product_variant_attributes", joinColumns = @JoinColumn(name = "product_variant_id"), inverseJoinColumns = @JoinColumn(name = "attribute_id"))
    @Builder.Default
    private Set<AttributeEntity> attributes = new HashSet<>();
}
