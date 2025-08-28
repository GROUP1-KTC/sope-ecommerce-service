package com.sope.sope_ecommerce_backend.services.patterns;

import com.sope.sope_ecommerce_backend.entities.Discount;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CoinBackDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal applyDiscount(Discount discount, BigDecimal orderTotal, BigDecimal shippingCharges) {
        BigDecimal discountValue = calculateDiscountValue(discount, orderTotal);

        return discountValue;
    }
}
