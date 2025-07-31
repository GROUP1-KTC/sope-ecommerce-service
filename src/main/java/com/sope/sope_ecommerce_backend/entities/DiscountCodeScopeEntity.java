package com.sope.sope_ecommerce_backend.entities;

import com.sope.sope_ecommerce_backend.enums.ScopeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "discount_code_scopes")
public class DiscountCodeScopeEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "discount_code_id")
    private DiscountCodeEntity discountCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "scope_type", nullable = false)
    private ScopeType scopeType;

    @Column(name = "target_id", nullable = false)
    private UUID targetId;
}
