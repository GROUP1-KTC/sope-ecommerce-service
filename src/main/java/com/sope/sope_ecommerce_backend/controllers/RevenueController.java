package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.response.RevenueResponse;
import com.sope.sope_ecommerce_backend.services.RevenueService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/revenue")
public class RevenueController {

      private final RevenueService revenueService;

      @GetMapping("/{shopId}")
      public ResponseEntity<RevenueResponse> getShopRevenue(@PathVariable UUID shopId) {
            RevenueResponse revenueShop = revenueService.calculateShopRevenue(shopId);
            return ResponseEntity.ok(revenueShop);
      }

      @GetMapping
      public ResponseEntity<RevenueResponse> getAdminRevenue() {
            RevenueResponse revenueAdmin = revenueService.calculateAdminRevenue();
            return ResponseEntity.ok(revenueAdmin);

      }

}
