package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.request.ShopAddressRequest;
import com.sope.sope_ecommerce_backend.dto.response.ShopAddressResponse;
import com.sope.sope_ecommerce_backend.security.user.CustomUserDetails;
import com.sope.sope_ecommerce_backend.services.ShopAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/shop/address")
@RequiredArgsConstructor
public class ShopAddressController {

    private final ShopAddressService service;

    @PutMapping("/{addressId}")
    public ResponseEntity<ShopAddressResponse> updateAddress(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID addressId,
            @RequestBody ShopAddressRequest request) {
        return ResponseEntity.ok(service.updateAddress(userDetails.getUserId(), addressId, request));
    }

    @GetMapping
    public ResponseEntity<ShopAddressResponse> getAddress(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(service.getAddress(userDetails.getUserId()));
    }


    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID addressId) {
        service.deleteAddress(userDetails.getUserId(), addressId);
        return ResponseEntity.noContent().build();
    }
}

