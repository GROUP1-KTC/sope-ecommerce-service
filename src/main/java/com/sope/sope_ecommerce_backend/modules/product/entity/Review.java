package com.sope.sope_ecommerce_backend.modules.product.entity;

import com.sope.sope_ecommerce_backend.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Nullable for guest reviews

    @Column
    private String comment;

    @Column
    private LocalDateTime createdAt;
}
