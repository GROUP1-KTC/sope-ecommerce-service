package com.sope.sope_ecommerce_backend.modules.user.entity;

import com.sope.sope_ecommerce_backend.modules.user.enums.PackageType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "shop_priority_packages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopPriorityPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "priority_score", nullable = false)
    private int priorityScore;

    @Column(name = "package_type", length = 50)
    private PackageType packageType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}