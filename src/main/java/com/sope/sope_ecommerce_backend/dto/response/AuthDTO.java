package com.sope.sope_ecommerce_backend.dto.response;
public class AuthDTO {
    private String username;
    private String role;
    private String token;

    public AuthDTO() {

    }

    public AuthDTO(String username, String role, String token) {
        this.username = username;
        this.role = role;
        this.token = token;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}