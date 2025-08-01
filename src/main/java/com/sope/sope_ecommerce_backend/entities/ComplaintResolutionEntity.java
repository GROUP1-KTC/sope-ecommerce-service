package com.sope.sope_ecommerce_backend.entities;

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
@Table(name = "complaint_resolutions")

public class ComplaintResolutionEntity {
      @Id
      @GeneratedValue(strategy = GenerationType.UUID)
      @Column(name = "resolution_id")
      private UUID resolutionId;

      @ManyToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "complaint_id", nullable = false)
      private ComplaintEntity complaint;

      @Column(columnDefinition = "TEXT", nullable = false)
      private String note;

      @Builder.Default
      @Column(name = "resolved_at", nullable = false)
      private LocalDateTime resolvedAt = LocalDateTime.now();
}
