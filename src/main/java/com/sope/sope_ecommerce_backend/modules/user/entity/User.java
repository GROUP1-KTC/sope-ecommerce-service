package com.sope.sope_ecommerce_backend.modules.user.entity;

import com.sope.sope_ecommerce_backend.modules.order.entity.Order;
import com.sope.sope_ecommerce_backend.modules.product.entity.Rating;
import com.sope.sope_ecommerce_backend.modules.product.entity.Review;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    @Column(nullable = false)
    private String fullName;


    @Column
    private String phone;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Address> addresses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Rating> ratings;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private LoyaltyPoint loyaltyPoint;

}
