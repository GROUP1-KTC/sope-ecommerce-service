package com.sope.sope_ecommerce_backend.dto.request;

public record GuestInfo( String fullName,
                         String email,
                         String phone,
                         String shippingAddress,
                         String city,
                         String district,
                         String ward) {
}
