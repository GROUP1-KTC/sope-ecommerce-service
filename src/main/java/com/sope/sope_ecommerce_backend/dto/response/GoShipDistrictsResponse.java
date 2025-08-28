package com.sope.sope_ecommerce_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GoShipDistrictsResponse(
        String code,
        String status,
        List<DataDistrictsResponse> data
) {
    public record DataDistrictsResponse(
            String id,
            String name,

            @JsonProperty("city_id")
            String cityId
    ) {
    }
}
