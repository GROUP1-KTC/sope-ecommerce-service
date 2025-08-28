package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.request.DiscountCreateRequest;
import com.sope.sope_ecommerce_backend.dto.response.DiscountResponse;
import com.sope.sope_ecommerce_backend.entities.Discount;

import java.math.BigDecimal;
import java.util.Optional;

public interface DiscountService {
    DiscountResponse createDiscount(DiscountCreateRequest discount);
    DiscountResponse getDiscountByCode(String code);

    Discount getDiscountEntityByCode(String code);
    boolean validateDiscount(String code, BigDecimal orderTotal);
    BigDecimal applyDiscount(Discount discount, BigDecimal orderTotal, BigDecimal shippingCharges);
}
