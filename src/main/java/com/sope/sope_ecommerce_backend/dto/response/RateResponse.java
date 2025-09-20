package com.sope.sope_ecommerce_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


public record RateResponse(
        int code,
        String status,
        List<ShippingRate> data
) {
    public record ShippingRate(
            String id,
            @JsonAlias("carrier_name") String carrierName,
            @JsonAlias("carrier_logo") String carrierLogo,
            String service,
            String expected,
            @JsonAlias("cod_fee") String codFee,
            @JsonAlias("total_fee") String totalFee,
            @JsonAlias("total_amount") String totalAmount
    ) {}
}