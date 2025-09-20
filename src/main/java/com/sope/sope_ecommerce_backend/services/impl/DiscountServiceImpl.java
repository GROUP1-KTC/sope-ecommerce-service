package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.DiscountCreateRequest;
import com.sope.sope_ecommerce_backend.dto.response.DiscountResponse;
import com.sope.sope_ecommerce_backend.entities.Discount;
import com.sope.sope_ecommerce_backend.entities.Shop;
import com.sope.sope_ecommerce_backend.enums.DiscountScope;
import com.sope.sope_ecommerce_backend.mapper.DiscountMapper;
import com.sope.sope_ecommerce_backend.repositories.DiscountRepository;
import com.sope.sope_ecommerce_backend.repositories.specification.DiscountSpecification;
import com.sope.sope_ecommerce_backend.services.DiscountService;
import com.sope.sope_ecommerce_backend.services.ShopService;
import com.sope.sope_ecommerce_backend.services.patterns.DiscountStrategy;
import com.sope.sope_ecommerce_backend.services.patterns.DiscountStrategyFactory;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;
    private final DiscountStrategyFactory discountStrategyFactory;
    private final DiscountMapper discountMapper;
    private final ShopService shopService;

    @Override
    @Transactional
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

        if (request.scope() == DiscountScope.SHOP) {
            if (request.shopId() == null) {
                throw new IllegalArgumentException("ShopId is required for SHOP discount");
            }
            Shop shop = shopService.getShopEntityById(request.shopId());
            shop.addDiscount(newDiscount);
        }

        Discount savedDiscount = discountRepository.save(newDiscount);
        return discountMapper.toResponse(savedDiscount);
    }

    @Override
    public List<DiscountResponse> getAllDiscounts() {
        List<Discount> discounts = discountRepository.findAll();
        return discountMapper.toResponseList(discounts);
    }

    @Override
    public List<DiscountResponse> getActiveDiscountsByShop(UUID shopId) {
        List<Discount> discounts = discountRepository.findAll(
                Specification.allOf(DiscountSpecification.byShopId(shopId))
                        .and(DiscountSpecification.isActive(LocalDateTime.now())));

        return discountMapper.toResponseList(discounts);
    }

    @Override
    public Page<DiscountResponse> getDiscountOfShop(UUID shopId, int page, int size){
        Page<Discount> discounts = discountRepository.findAll(
                Specification.allOf(DiscountSpecification.byShopId(shopId)),
                PageRequest.of(page, size)
        );
        return discounts.map(discountMapper::toResponse);
    }

    @Override
    public List<DiscountResponse> getActiveDiscountsOfPlatform() {
        List<Discount> discounts = discountRepository.findAll(
                Specification.allOf(DiscountSpecification.isActive(LocalDateTime.now()))
                        .and(DiscountSpecification.byScopes(
                                DiscountScope.PLATFORM,
                                DiscountScope.FREESHIP,
                                DiscountScope.COIN_BACK
                        ))
        );

        return discountMapper.toResponseList(discounts);
    }

    @Override
    public List<DiscountResponse> getAllDiscountsOfPlatform() {
        List<Discount> discounts = discountRepository.findByScopeIn(List.of(DiscountScope.PLATFORM, DiscountScope.FREESHIP, DiscountScope.COIN_BACK));
        return discountMapper.toResponseList(discounts);
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
    @Transactional
    public DiscountResponse updateDiscount(UUID discountId, DiscountCreateRequest request) {
        Discount existingDiscount = discountRepository.findById(discountId).orElseThrow(
                () -> new IllegalArgumentException("Discount with id " + discountId + " not found")
        );

        if (!existingDiscount.getCode().equals(request.code())) {
            Optional<Discount> discountWithSameCode = discountRepository.findByCode(request.code());
            if (discountWithSameCode.isPresent()) {
                Discount discount = discountWithSameCode.get();
                LocalDateTime now = LocalDateTime.now();
                boolean isNotExpired = discount.getEndDate() == null || !now.isAfter(discount.getEndDate());
                boolean isNotUsedUp = discount.getMaxUsage() == 0 || discount.getCurrentUsage() < discount.getMaxUsage();
                boolean isNotYetActive = discount.getStartDate() != null && now.isBefore(discount.getStartDate());

                if (isNotExpired && isNotUsedUp || isNotYetActive) {
                    throw new IllegalArgumentException("Discount code " + request.code() +
                            (isNotYetActive ? " is not yet active" : " is still active or has not expired"));
                }
            }
        }

        if(existingDiscount.getScope() != request.scope()) {
            throw new IllegalArgumentException("Cannot change discount scope");
        }

        Discount updatedDiscount = discountMapper.toEntity(request);
        updatedDiscount.setId(existingDiscount.getId());
        updatedDiscount.setCurrentUsage(existingDiscount.getCurrentUsage());

        if (request.scope() == DiscountScope.SHOP) {
            if (request.shopId() == null) {
                throw new IllegalArgumentException("ShopId is required for SHOP discount");
            }
            Shop shop = shopService.getShopEntityById(request.shopId());
            shop.addDiscount(updatedDiscount);
        }


        Discount savedDiscount = discountRepository.save(updatedDiscount);
        return discountMapper.toResponse(savedDiscount);
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
