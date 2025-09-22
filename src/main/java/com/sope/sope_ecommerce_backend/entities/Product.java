package com.sope.sope_ecommerce_backend.entities;

import com.sope.sope_ecommerce_backend.enums.StatusProduct;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private String brand;

    @Column(length = 3000)
    private String description;

    @Column(nullable = false)
    private String defaultImage;

    private String defaultVideoIntro;

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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishlistEntity> wishlists;

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductDetailEntity> productDetails = new ArrayList<>();

    @Column(name = "embedding", columnDefinition = "float8[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private float[] embedding;

    // ===== Many-to-Many relationship for users who suggest this product =====
    @ManyToMany
    @JoinTable(
            name = "product_suggested_for_user",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Builder.Default
    private List<AppUser> suggestedByUsers = new ArrayList<>();

    // ===== Many-to-Many relationship for similar products =====
    @ManyToMany
    @JoinTable(
            name = "product_similarities",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "similar_product_id")
    )
    @Builder.Default
    private List<Product> similarProducts = new ArrayList<>();

    // ===== Many-to-Many relationship for suggested products =====
    @ManyToMany
    @JoinTable(
            name = "product_suggestions",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "suggested_product_id")
    )
    @Builder.Default
    private List<Product> suggestedProducts = new ArrayList<>();

    @Column(length = 2000)
    private String overallReview;

}
