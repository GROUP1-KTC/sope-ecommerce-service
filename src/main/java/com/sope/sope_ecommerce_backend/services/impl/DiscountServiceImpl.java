package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.entities.Discount;
import com.sope.sope_ecommerce_backend.repositories.DiscountRepository;
import com.sope.sope_ecommerce_backend.services.DiscountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@AllArgsConstructor
@Service
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;

    @Override
    public Discount createDiscount(Discount discount) {
        return discountRepository.save(discount);
    }

    @Override
    public Optional<Discount> getDiscountByCode(String code) {
        return discountRepository.findByCode(code);
    }

    @Override
    public boolean validateDiscount(String code, BigDecimal orderTotal) {
        Optional<Discount> discountOpt = discountRepository.findByCode(code);
        if (discountOpt.isEmpty()) return false;

        Discount discount = discountOpt.get();

        // check active
        if (!discount.isActive()) return false;

        // check min order
        if (discount.getMinOrderValue() != null && orderTotal.compareTo(discount.getMinOrderValue()) < 0) return false;

        return true;
    }

    @Override
    public BigDecimal applyDiscount(Discount discount, BigDecimal orderTotal, BigDecimal shippingCharges) {
        if (!validateDiscount(discount.getCode(), orderTotal)) {
            return BigDecimal.ZERO;
        }

        BigDecimal discountValueForOrder = BigDecimal.ZERO;

        switch (discount.getScope()) {
            case SHOP, PLATFORM -> {
                discountValueForOrder = calculateDiscountValue(discount, orderTotal);
            }
            case FREESHIP -> {
                discountValueForOrder = calculateDiscountValue(discount, shippingCharges);
            }
        }

        // update current usage
        discount.setCurrentUsage(discount.getCurrentUsage() + 1);
        discountRepository.save(discount);

        return discountValueForOrder;
    }



    private BigDecimal calculateDiscountValue(Discount discount, BigDecimal baseAmount) {
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

        if (discountValue.compareTo(baseAmount) > 0) {
            discountValue = baseAmount;
        }

        return discountValue;
    }


}
