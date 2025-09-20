package com.sope.sope_ecommerce_backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;


@Builder
public record ShipmentRequest(ShipmentDetails shipment) {
    public record ShipmentDetails(
            @JsonProperty("address_from") Address addressFrom,
            @JsonProperty("address_to") Address addressTo,
            Parcel parcel
    ) {}

    public record Address(
            String city,
            String district,
            String ward
    ) {}

    public record Parcel(
            String cod,
            String amount,
            String weight,
            String width,
            String height,
            String length
    ) {}
}