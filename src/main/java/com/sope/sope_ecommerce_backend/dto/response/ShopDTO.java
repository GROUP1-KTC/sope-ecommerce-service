package com.sope.sope_ecommerce_backend.dto.response;

import com.sope.sope_ecommerce_backend.entities.User;

import java.util.UUID;

public class ShopDTO {
    private UUID id;

    private User user;

    private String name;

    private String phone;

    private String email;

    private String address;

    private String description;

    private String logoUrl;

    private boolean isMall;

    private String status;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public boolean isMall() {
        return isMall;
    }

    public void setMall(boolean mall) {
        isMall = mall;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
