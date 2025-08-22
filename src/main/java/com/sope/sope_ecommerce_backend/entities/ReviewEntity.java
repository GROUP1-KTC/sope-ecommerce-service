package com.sope.sope_ecommerce_backend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reviews", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "product_id" }) })
public class ReviewEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "review_id")
        private Long reviewId;

        @ManyToOne
        @JoinColumn(name = "product_id", nullable = false)
        private Product product;

        @ManyToOne
        @JoinColumn(name = "user_id")
        @JsonBackReference
        private AppUser appUser;

        private Integer rating;

        private String content;

        @Builder.Default
        @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<ImageEntity> imagesListReview = new ArrayList<>();

        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @Column(name = "updated_at")
        private LocalDateTime updatedAt;
}
