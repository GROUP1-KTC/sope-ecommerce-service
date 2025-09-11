package com.sope.sope_ecommerce_backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_variants")
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_variant_id", columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID productVariantId;

    @Column(nullable = false)
    private BigDecimal price;

    private int stock;

    private int sold;

    private String imageVariant;

    @Embedded
    private Dimension dimension;

    private BigDecimal weight;

    // @Column(name = "sku", unique = true, nullable = false, length = 100)
    // private String sku;

    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReviewEntity> reviews = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "product_variant_attributes", joinColumns = @JoinColumn(name = "product_variant_id"), inverseJoinColumns = @JoinColumn(name = "attribute_id"))
    @OrderColumn(name = "attribute_order")
    @Builder.Default
    private List<Attribute> attributes = new ArrayList<>();

}
