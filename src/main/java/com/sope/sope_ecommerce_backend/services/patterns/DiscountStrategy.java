package com.sope.sope_ecommerce_backend.services.patterns;

import com.sope.sope_ecommerce_backend.entities.Discount;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface DiscountStrategy {
    BigDecimal applyDiscount(Discount discount, BigDecimal orderTotal, BigDecimal shippingCharges);

    default BigDecimal calculateDiscountValue(Discount discount, BigDecimal baseAmount) {
        BigDecimal discountValue = BigDecimal.ZERO;

        switch (discount.getDiscountType()) {
            case PERCENTAGE -> {
                BigDecimal percent = discount.getDiscountValue()
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                discountValue = baseAmount.multiply(percent);
                if (discount.getMaxDiscountValue() != null) {
                    discountValue = discountValue.min(discount.getMaxDiscountValue());
                }
            }
            case FIXED_AMOUNT -> {
                discountValue = discount.getDiscountValue();
            }
        }

        return discountValue.compareTo(baseAmount) > 0 ? baseAmount : discountValue;
    }
}