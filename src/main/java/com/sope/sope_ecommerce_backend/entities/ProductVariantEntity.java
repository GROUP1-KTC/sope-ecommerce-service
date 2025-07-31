package com.sope.sope_ecommerce_backend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class ProductVariantEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "product_variant_id", columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID productVariantId;

    @Column(nullable = false)
    private BigDecimal price;

    private int stock;

    private int sold;

    private boolean hidden;

    @Column(name = "slug", length = 100, unique = true)
    private String slug;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @ManyToMany
    @JoinTable(name = "product_variant_attributes", joinColumns = @JoinColumn(name = "product_variant_id"), inverseJoinColumns = @JoinColumn(name = "attribute_id"))

    @Builder.Default
    private Set<AttributeEntity> attributes = new HashSet<>();

    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageEntity> images;
}
