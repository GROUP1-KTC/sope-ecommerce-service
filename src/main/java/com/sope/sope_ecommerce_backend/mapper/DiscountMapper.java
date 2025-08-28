package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.request.DiscountCreateRequest;
import com.sope.sope_ecommerce_backend.dto.response.DiscountResponse;
import com.sope.sope_ecommerce_backend.entities.Discount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DiscountMapper {


    @Mapping(target = "discountValue", source = "value")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "currentUsage", ignore = true)
    @Mapping(target = "orderDiscounts", ignore = true)
    Discount toEntity(DiscountCreateRequest request);

    @Mapping(source = "discountValue", target = "value")
    DiscountResponse toResponse(Discount discount);
}
