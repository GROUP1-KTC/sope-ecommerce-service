package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.BasePaymentDTO;
import com.sope.sope_ecommerce_backend.entities.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "payUrl", source = "providerPayUrl")
    BasePaymentDTO toBasePaymentDTO(Payment payment);
}
