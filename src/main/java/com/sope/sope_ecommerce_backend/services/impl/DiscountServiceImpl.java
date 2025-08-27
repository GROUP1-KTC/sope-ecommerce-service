package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.DiscountCreateRequest;
import com.sope.sope_ecommerce_backend.dto.response.DiscountResponse;
import com.sope.sope_ecommerce_backend.entities.Discount;
import com.sope.sope_ecommerce_backend.mapper.DiscountMapper;
import com.sope.sope_ecommerce_backend.repositories.DiscountRepository;
import com.sope.sope_ecommerce_backend.services.DiscountService;
import com.sope.sope_ecommerce_backend.services.patterns.DiscountStrategy;
import com.sope.sope_ecommerce_backend.services.patterns.DiscountStrategyFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Service
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;
    private final DiscountStrategyFactory discountStrategyFactory;
    private final DiscountMapper discountMapper;

    @Override
    public DiscountResponse createDiscount(DiscountCreateRequest request) {
        Optional<Discount> existingDiscount = discountRepository.findByCode(request.code());

        if (existingDiscount.isPresent()) {
            Discount discount = existingDiscount.get();
            LocalDateTime now = LocalDateTime.now();
            boolean isNotExpired = discount.getEndDate() == null || !now.isAfter(discount.getEndDate());
            boolean isNotUsedUp = discount.getMaxUsage() == 0 || discount.getCurrentUsage() < discount.getMaxUsage();
            boolean isNotYetActive = discount.getStartDate() != null && now.isBefore(discount.getStartDate());

            if (isNotExpired && isNotUsedUp || isNotYetActive) {
                throw new IllegalArgumentException("Discount code " + request.code() +
                        (isNotYetActive ? " is not yet active" : " is still active or has not expired"));
            }
        }

        Discount newDiscount = discountMapper.toEntity(request);

        Discount savedDiscount = discountRepository.save(newDiscount);

        return discountMapper.toResponse(savedDiscount);
    }

    @Override
    public DiscountResponse getDiscountByCode(String code) {

        Discount discount = discountRepository.findByCode(code).orElseThrow(
                () -> new IllegalArgumentException("Discount code " + code + " not found")
        );

        return discountMapper.toResponse(discount);
    }

    @Override
    public Discount getDiscountEntityByCode(String code) {

        Discount discount = discountRepository.findByCode(code).orElseThrow(
                () -> new IllegalArgumentException("Discount code " + code + " not found")
        );

        return discount;
    }

    @Override
    public boolean validateDiscount(String code, BigDecimal orderTotal) {
        Discount discount = discountRepository.findByCode(code).orElseThrow(
                () -> new IllegalArgumentException("Discount code " + code + " not found")
        );

        return discount.isActive() && isOrderValueValid(discount, orderTotal);
    }

    @Override
    public BigDecimal applyDiscount(Discount discount, BigDecimal orderTotal, BigDecimal shippingCharges) {
        if (!validateDiscount(discount.getCode(), orderTotal)) {
            throw new IllegalArgumentException("Discount code " + discount.getCode() + " is not valid");
        }

        DiscountStrategy strategy = discountStrategyFactory.getStrategy(discount.getScope());
        BigDecimal discountValue = strategy.applyDiscount(discount, orderTotal, shippingCharges);

        // update current usage
        discount.setCurrentUsage(discount.getCurrentUsage() + 1);
        discountRepository.save(discount);

        return discountValue;
    }

    private boolean isOrderValueValid(Discount discount, BigDecimal orderTotal) {
        return discount.getMinOrderValue() == null ||
                orderTotal.compareTo(discount.getMinOrderValue()) >= 0;
    }
}
