package com.sope.sope_ecommerce_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ShipmentResponse(
        int code,
        String status,
        String id,
        String cod,
        String fee,
        @JsonProperty("tracking_number") String trackingNumber,
        String carrier,
        @JsonProperty("carrier_short_name") String carrierShortName,
        @JsonProperty("created_at") String createdAt
) {}