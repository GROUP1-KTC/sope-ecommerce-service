package com.sope.sope_ecommerce_backend.dto.response;

import java.util.List;

public record GoShipCitiesResponse(
    String code,
    String status,
    List<DataCitiesResponse> data
) {
    public record DataCitiesResponse(
        String id,
        String name
    ) {
    }
}
