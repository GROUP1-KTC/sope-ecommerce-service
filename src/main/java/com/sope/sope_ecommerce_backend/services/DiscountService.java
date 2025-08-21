package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.entities.Discount;

import java.math.BigDecimal;
import java.util.Optional;

public interface DiscountService {
    Discount createDiscount(Discount discount);
    Optional<Discount> getDiscountByCode(String code);
    boolean validateDiscount(String code, BigDecimal orderTotal);
    BigDecimal applyDiscount(Discount discount, BigDecimal orderTotal, BigDecimal shippingCharges);
}
