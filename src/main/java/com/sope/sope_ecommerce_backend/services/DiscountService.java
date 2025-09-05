package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.request.DiscountCreateRequest;
import com.sope.sope_ecommerce_backend.dto.response.DiscountResponse;
import com.sope.sope_ecommerce_backend.entities.Discount;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DiscountService {
    DiscountResponse createDiscount(DiscountCreateRequest discount);
    DiscountResponse getDiscountByCode(String code);


    List<DiscountResponse> getAllDiscounts();

    List<DiscountResponse> getAllDiscountsOfPlatform();

    List<DiscountResponse> getActiveDiscountsOfPlatform();


    Discount getDiscountEntityByCode(String code);
    boolean validateDiscount(String code, BigDecimal orderTotal);
    BigDecimal applyDiscount(Discount discount, BigDecimal orderTotal, BigDecimal shippingCharges);

    List<DiscountResponse> getActiveDiscountsByShop(UUID shopId);

    DiscountResponse updateDiscount(UUID discountId, DiscountCreateRequest request);
}
