package com.sope.sope_ecommerce_backend.entities;

// import com.sope.sope_ecommerce_backend.enums.ReturnStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "return_request")
public class ReturnOrder {
      @Id
      @GeneratedValue(strategy = GenerationType.UUID)
      @Column(name = "return_request_id")
      private UUID returnRequestId;

      @ManyToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "order_id", nullable = false)
      private Order order;

      @ManyToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "user_id", nullable = false)
      private AppUser user;

      @Column(nullable = false, columnDefinition = "TEXT")
      private String reason;

      // @Builder.Default
      // private ReturnStatus status = ReturnStatus.PENDING;

      @Column(name = "total_refund_amount", precision = 10, scale = 2)
      private BigDecimal totalRefundAmount;

      @Column(name = "note", columnDefinition = "TEXT")
      private String note;

      @Builder.Default
      @Column(name = "created_at")
      private LocalDateTime createdAt = LocalDateTime.now();

      @Column(name = "updated_at")
      private LocalDateTime updatedAt;

      @PreUpdate
      protected void onUpdate() {
            this.updatedAt = LocalDateTime.now();
      }
}