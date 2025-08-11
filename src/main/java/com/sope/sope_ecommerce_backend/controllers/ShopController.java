package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.request.ShopCreateRequest;
import com.sope.sope_ecommerce_backend.dto.request.ShopUpdateRequest;
import com.sope.sope_ecommerce_backend.dto.response.ShopResponse;
import com.sope.sope_ecommerce_backend.dto.response.ShopSearchResult;
import com.sope.sope_ecommerce_backend.entities.Shop;
import com.sope.sope_ecommerce_backend.security.CustomUserDetails;
import com.sope.sope_ecommerce_backend.services.ShopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;


    @PostMapping
    public ResponseEntity<ShopResponse> createShop(
            @Valid @RequestBody ShopCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        ShopResponse createdShop = shopService.createShop(request, currentUser.getUserId());
        return ResponseEntity.ok(createdShop);
    }

    @GetMapping("/me")
    public ResponseEntity<ShopResponse> getShop(@AuthenticationPrincipal CustomUserDetails currentUser) {
        ShopResponse getShop = shopService.getShop(currentUser.getUserId());
        return ResponseEntity.ok(getShop);
    }

    @GetMapping
    public ResponseEntity<List<ShopResponse>> getAllShop() {
        List<ShopResponse> getAllShops = shopService.getAllShops();
        return ResponseEntity.ok(getAllShops);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShopResponse> getShopById(UUID shopId) {
        ShopResponse getShop = shopService.getShopById(shopId);
        return ResponseEntity.ok(getShop);
    }

    @PatchMapping()
    public ResponseEntity<ShopResponse> updateShop(
            @RequestBody ShopUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        return ResponseEntity.ok(
                shopService.updateShop(request, currentUser.getUserId())
        );
    }

    @PatchMapping("/status")
    public ResponseEntity<ShopResponse> updateShopStatus(UUID shopId, Shop.Status status) {
        return ResponseEntity.ok(shopService.changeShopStatus(shopId, status));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ShopSearchResult>> searchShops(@RequestParam String name) {
        return ResponseEntity.ok(shopService.searchShopsByName(name));
    }



}
