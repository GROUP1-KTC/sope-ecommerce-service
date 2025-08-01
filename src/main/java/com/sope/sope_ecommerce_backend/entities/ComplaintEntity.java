package com.sope.sope_ecommerce_backend.entities;

import com.sope.sope_ecommerce_backend.enums.ComplaintStatus;
import com.sope.sope_ecommerce_backend.enums.TargetType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "complaints")
public class ComplaintEntity {
      // FROM CUSTOMER -> ADMIN
      @Id
      @GeneratedValue(strategy = GenerationType.UUID)
      @Column(name = "complaint_id")
      private UUID complaintId;

      @ManyToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "user_id", nullable = false)
      private UserEntity user;

      @Column(name = "target_id", nullable = false)
      private UUID targetId;

      @Enumerated(EnumType.STRING)
      @Column(name = "target_type", nullable = false)
      private TargetType targetType;

      @Column(nullable = false, columnDefinition = "TEXT")
      private String description;

      @Enumerated(EnumType.STRING)
      @Builder.Default
      private ComplaintStatus status = ComplaintStatus.PENDING;

      @Builder.Default
      @Column(name = "created_at")
      private LocalDateTime createdAt = LocalDateTime.now();

      @Column(name = "resolution_date")
      private LocalDateTime resolutionDate;

      @OneToOne(mappedBy = "complaint", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
      private ComplaintResolutionEntity resolution;

}
