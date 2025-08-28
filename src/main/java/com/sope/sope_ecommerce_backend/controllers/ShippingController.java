package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.ApiResponse;
import com.sope.sope_ecommerce_backend.dto.request.ShipmentCreationRequest;
import com.sope.sope_ecommerce_backend.dto.request.ShipmentRequest;
import com.sope.sope_ecommerce_backend.dto.response.RateResponse;
import com.sope.sope_ecommerce_backend.dto.response.ShipmentResponse;
import com.sope.sope_ecommerce_backend.services.gateways.GoShippingGateway;
import com.sope.sope_ecommerce_backend.utils.ApiResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/shipping")
public class ShippingController {

    private final GoShippingGateway goShippingGateway;

    @PostMapping("/rates")
    public ResponseEntity<ApiResponse<RateResponse>> getShippingRates(@Valid @RequestBody ShipmentRequest request) {
        try {
            RateResponse rates = goShippingGateway.getShippingRates(request);
            return ApiResponseUtil.success(rates, "Fetched shipping rates successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to fetch shipping rates", List.of(e.getMessage()));
        }
    }

    @PostMapping("/sync")
    public ResponseEntity<ApiResponse<String>> syncShippingData() {
        try {
            goShippingGateway.syncLocations();
            return ApiResponseUtil.success(null, "Synchronized shipping data successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to synchronize shipping data", List.of(e.getMessage()));
        }
    }

    @PostMapping("/shipments")
    public ResponseEntity<ShipmentResponse> createShipment(@Valid @RequestBody ShipmentCreationRequest request) {
        try {
            ShipmentResponse response = goShippingGateway.createShipment(request);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }


//    @PostMapping("/webhooks/goship")
//    public ResponseEntity<Void> handleWebhook(@RequestBody Object webhookPayload) {
//        log.info("Received Goship webhook: {}", webhookPayload);
//        // TODO: Implement webhook processing logic (e.g., update order status in database)
//        return ResponseEntity.ok().build();
//    }
}
