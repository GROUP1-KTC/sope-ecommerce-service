package com.sope.sope_ecommerce_backend.modules.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String details;

    @NotBlank(message = "village cannot be empty")
    @Column(nullable = false)
    private String village;

    @Column
    private String city;

    @Column
    private String country;

    @Column
    private boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
