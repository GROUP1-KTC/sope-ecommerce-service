package com.sope.sope_ecommerce_backend.modules.product.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.GenericGenerator;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double defaultPrice;

    @Column
    private String brand;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String defaultImage;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProductVariant> variants; // At least 2 variants per product

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Rating> ratings;
}

