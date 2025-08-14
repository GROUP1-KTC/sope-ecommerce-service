package com.sope.sope_ecommerce_backend.entities;

import com.sope.sope_ecommerce_backend.enums.StatusProduct;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id", columnDefinition = "UUID", updatable = false, nullable = false)

    private UUID productId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal defaultPrice;

    private String brand;

    @Column(length = 3000)
    private String description;

    @Column(nullable = false)
    private String defaultImage;

    private String defaultVideoIntro;

    private int stock;

    private int sold;

    private boolean hidden;

    @Enumerated(EnumType.STRING)
    private StatusProduct status;

    @Column(name = "slug", length = 100, unique = true)
    private String slug;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariant> variants;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageEntity> imagesList;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ReviewEntity> reviews;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishlistEntity> wishlists;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductDetailEntity> productDetails;
}
