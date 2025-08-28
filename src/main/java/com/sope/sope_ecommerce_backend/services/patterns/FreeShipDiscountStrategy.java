package com.sope.sope_ecommerce_backend.services.patterns;

import com.sope.sope_ecommerce_backend.entities.Discount;
import com.sope.sope_ecommerce_backend.services.patterns.DiscountStrategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FreeShipDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal applyDiscount(Discount discount, BigDecimal orderTotal, BigDecimal shippingCharges) {
        return calculateDiscountValue(discount, shippingCharges);
    }
}