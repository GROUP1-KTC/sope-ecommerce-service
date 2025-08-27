package com.sope.sope_ecommerce_backend.entities;

import com.sope.sope_ecommerce_backend.enums.ComplaintTargetType;
import com.sope.sope_ecommerce_backend.enums.ComplaintStatus;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "complaints")
public class ComplaintEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  // Người báo cáo (có thể là buyer hoặc seller hoặc admin)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reporter_id", nullable = false)
  private AppUser reporter;

  @Enumerated(EnumType.STRING)
  @Column(name = "target_type", nullable = false)
  private ComplaintTargetType targetType;

  // ID tham chiếu của đối tượng bị báo cáo (có thể null nếu targetType = ADMIN)
  @Column(name = "target_id")
  private UUID targetId;

  @Column(name = "reason", columnDefinition = "TEXT", nullable = false)
  private String reason;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private ComplaintStatus statusComplaint;

  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "solved_at")
  private LocalDateTime solvedAt;
}
