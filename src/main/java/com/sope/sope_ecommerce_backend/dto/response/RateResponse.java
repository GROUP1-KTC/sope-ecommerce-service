package com.sope.sope_ecommerce_backend.dto.response;

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
            @JsonProperty("carrier_name") String carrierName,
            @JsonProperty("carrier_logo") String carrierLogo,
            String service,
            String expected,
            @JsonProperty("cod_fee") String codFee,
            @JsonProperty("total_fee") String totalFee,
            @JsonProperty("total_amount") String totalAmount
    ) {}
}