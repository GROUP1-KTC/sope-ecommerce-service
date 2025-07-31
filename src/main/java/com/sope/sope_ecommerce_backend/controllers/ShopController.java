package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.response.ShopDTO;
import com.sope.sope_ecommerce_backend.services.ShopService;
import com.sope.sope_ecommerce_backend.entities.UserEntity;
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
    public ResponseEntity<ShopDTO> createShop(@Valid @RequestBody ShopDTO shopDTO,
                                              @AuthenticationPrincipal UserEntity user) {
        ShopDTO createdShop = shopService.createShop(shopDTO, user);
        return ResponseEntity.ok(createdShop);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShopDTO> getShopById(@PathVariable UUID id) {
        ShopDTO shop = shopService.getShopById(id);
        return ResponseEntity.ok(shop);
    }

    @GetMapping
    public ResponseEntity<List<ShopDTO>> getAllShops() {
        List<ShopDTO> shops = shopService.getAllShops();
        return ResponseEntity.ok(shops);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShopDTO> updateShop(@PathVariable UUID id,
                                              @Valid @RequestBody ShopDTO shopDTO) {
        ShopDTO updatedShop = shopService.updateShop(id, shopDTO);
        return ResponseEntity.ok(updatedShop);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShop(@PathVariable UUID id) {
        shopService.deleteShop(id);
        return ResponseEntity.noContent().build();
    }
}
