package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.request.DiscountCreateRequest;
import com.sope.sope_ecommerce_backend.dto.response.DiscountResponse;
import com.sope.sope_ecommerce_backend.entities.Discount;
import com.sope.sope_ecommerce_backend.enums.DiscountStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DiscountMapper {


    @Mapping(target = "discountValue", source = "value")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "currentUsage", ignore = true)
    @Mapping(target = "orderDiscounts", ignore = true)
    @Mapping(target = "discountType", defaultValue = "FIXED_AMOUNT")
    Discount toEntity(DiscountCreateRequest request);

    @Mapping(source = "discountValue", target = "value")
    @Mapping(target = "status", expression = "java(calculateStatus(discount))")
    DiscountResponse toResponse(Discount discount);

    List<DiscountResponse> toResponseList(List<Discount> discounts);


    default DiscountStatus calculateStatus(Discount discount) {
        LocalDateTime now = LocalDateTime.now();

        boolean expired = discount.getEndDate() != null && now.isAfter(discount.getEndDate());
        boolean notStarted = discount.getStartDate() != null && now.isBefore(discount.getStartDate());
        boolean usageExceeded = discount.getMaxUsage() > 0 && discount.getCurrentUsage() >= discount.getMaxUsage();

        if (expired || usageExceeded) return DiscountStatus.EXPIRED;
        if (notStarted) return DiscountStatus.INACTIVE;
        return DiscountStatus.ACTIVE;
    }
}
