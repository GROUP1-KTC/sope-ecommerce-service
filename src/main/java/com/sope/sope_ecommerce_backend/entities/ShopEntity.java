package com.sope.sope_ecommerce_backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class ShopEntity {

    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String address;

    @Column(columnDefinition = "Text")
    private String description;

    private String logoUrl;

    private boolean isMall;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    public enum Status {
        ACTIVE, INACTIVE, DELETED
    }

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    public ShopEntity() {}

    public ShopEntity(UUID id, User user, String name, String phone, String email, String address, String description, String logoUrl, boolean isMall, Status status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.description = description;
        this.logoUrl = logoUrl;
        this.isMall = isMall;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
