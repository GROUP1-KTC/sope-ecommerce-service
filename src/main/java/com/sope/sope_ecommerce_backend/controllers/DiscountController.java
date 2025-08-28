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
}
