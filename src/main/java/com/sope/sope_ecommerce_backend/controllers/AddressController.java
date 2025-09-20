package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.request.AddressCreateRequest;
import com.sope.sope_ecommerce_backend.dto.request.AddressUpdateRequest;
import com.sope.sope_ecommerce_backend.dto.response.AddressResponse;
import com.sope.sope_ecommerce_backend.security.user.CustomUserDetails;
import com.sope.sope_ecommerce_backend.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponse> addAddress(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody AddressCreateRequest request) {
        return ResponseEntity.ok(addressService.addAddress(userDetails.getUserId(), request));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID addressId,
            @RequestBody AddressUpdateRequest request) {
        return ResponseEntity.ok(addressService.updateAddress(userDetails.getUserId(), addressId, request));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID addressId) {
        addressService.deleteAddress(userDetails.getUserId(), addressId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getUserAddresses(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(addressService.getUserAddresses(userDetails.getUserId()));
    }

    @PutMapping("/{addressId}/default")
    public ResponseEntity<AddressResponse> setDefaultAddress(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID addressId) {
        return ResponseEntity.ok(addressService.setDefaultAddress(userDetails.getUserId(), addressId));
    }

    @GetMapping("/default")
    public ResponseEntity<AddressResponse> getDefaultAddress(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(addressService.getDefaultAddress(userDetails.getUserId()));
    }
}
