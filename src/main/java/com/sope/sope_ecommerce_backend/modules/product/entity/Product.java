package com.sope.sope_ecommerce_backend.modules.product.entity;

import com.sope.sope_ecommerce_backend.modules.product.enums.StatusProduct;
import com.sope.sope_ecommerce_backend.modules.review.entity.Review;
import com.sope.sope_ecommerce_backend.modules.user.entity.Shop;
import com.sope.sope_ecommerce_backend.modules.wishlist.entity.Wishlist;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariant> variants; // At least 2 variants per product

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wishlist> wishlists;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductDetail> productDetails;
}
