package com.sope.sope_ecommerce_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GoShipWardResponse(
        String code,
        String status,
        List<DataWardsResponse> data
) {

    public record DataWardsResponse(
            String id,
            String name,

            @JsonProperty("district_id")
            String districtId
    ) {
    }
}
