package com.sope.sope_ecommerce_backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "review_media")
public class ReviewMediaEntity {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      private int priority;

      private String url;

      @ManyToOne
      @JoinColumn(name = "review_id")
      private ReviewEntity review;
}
