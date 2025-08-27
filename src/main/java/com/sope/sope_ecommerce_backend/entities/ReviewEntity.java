package com.sope.sope_ecommerce_backend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reviews", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "product_id" }) })
public class ReviewEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        @Column(name = "review_id")
        private UUID reviewId;

        @ManyToOne
        @JoinColumn(name = "product_id", nullable = false)
        private Product product;

        @ManyToOne
        @JoinColumn(name = "user_id")
        @JsonBackReference
        private AppUser appUser;

        @Min(1)
        @Max(5)
        @Column(nullable = false)
        private Integer rating;

        private String content;

        @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<ReviewMediaEntity> mediaList = new ArrayList<>();

        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @Column(name = "updated_at")
        private LocalDateTime updatedAt;
}
