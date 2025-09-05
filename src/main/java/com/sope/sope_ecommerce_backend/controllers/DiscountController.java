package com.sope.sope_ecommerce_backend.controllers;

import com.cloudinary.Api;
import com.sope.sope_ecommerce_backend.dto.ApiResponse;
import com.sope.sope_ecommerce_backend.dto.request.DiscountCreateRequest;
import com.sope.sope_ecommerce_backend.dto.response.DiscountResponse;
import com.sope.sope_ecommerce_backend.services.DiscountService;
import com.sope.sope_ecommerce_backend.utils.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/discounts")
public class DiscountController {
    public final DiscountService discountService;

    @PostMapping()
    public ResponseEntity<ApiResponse<DiscountResponse>> createDiscount(
            @RequestBody DiscountCreateRequest request
            ) {
        try {

            DiscountResponse discountResponse = discountService.createDiscount(request);

            return  ApiResponseUtil.created(discountResponse, "Create discount successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to create discount", List.of(e.getMessage()));
        }
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<? extends DiscountResponse>>> getAllDiscounts() {
        try {
            List<? extends DiscountResponse> discounts = discountService.getAllDiscounts();

            return  ApiResponseUtil.success(discounts, "Get all discounts successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to get discounts", List.of(e.getMessage()));
        }
    }

    @GetMapping("/shops/{shopId}")
    public ResponseEntity<ApiResponse<List<? extends DiscountResponse>>> getAllDiscountsOfShop(
            @PathVariable UUID shopId
    ) {
        try {
            List<? extends DiscountResponse> discounts = discountService.getActiveDiscountsByShop(shopId);

            return  ApiResponseUtil.success(discounts, "Get all discounts successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to get discounts", List.of(e.getMessage()));
        }
    }

    @GetMapping("/platform")
    public ResponseEntity<ApiResponse<List<? extends DiscountResponse>>> getAllDiscountsOfPlatform() {
        try {
            List<? extends DiscountResponse> discounts = discountService.getAllDiscountsOfPlatform();

            return  ApiResponseUtil.success(discounts, "Get all discounts successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to get discounts", List.of(e.getMessage()));
        }
    }


    @GetMapping("/platform/active")
    public ResponseEntity<ApiResponse<List<? extends DiscountResponse>>> getActiveDiscountsOfPlatform() {
        try {
            List<? extends DiscountResponse> discounts = discountService.getActiveDiscountsOfPlatform();

            return  ApiResponseUtil.success(discounts, "Get all discounts successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to get discounts", List.of(e.getMessage()));
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<DiscountResponse>> getDiscountByCode(
            @PathVariable String code
    ) {
        try {
            DiscountResponse discountResponse = discountService.getDiscountByCode(code);

            return  ApiResponseUtil.success(discountResponse, "Get discount successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to get discount", List.of(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DiscountResponse>> updateDiscount(
            @PathVariable UUID id,
            @RequestBody DiscountCreateRequest request
    ) {
        try {
            DiscountResponse discountResponse = discountService.updateDiscount(id, request);

            return  ApiResponseUtil.success(discountResponse, "Update discount successfully.");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Failed to update discount", List.of(e.getMessage()));
        }
    }


}
