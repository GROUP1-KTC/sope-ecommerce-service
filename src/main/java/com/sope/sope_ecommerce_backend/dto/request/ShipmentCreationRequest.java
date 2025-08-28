package com.sope.sope_ecommerce_backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ShipmentCreationRequest(ShipmentDetails shipment) {
    public record ShipmentDetails(
            String rate,
            int payer, // 1: Sender, 0: Receiver
            @JsonProperty("address_from") Address addressFrom,
            @JsonProperty("address_to") Address addressTo,
            Parcel parcel
    ) {}

    public record Address(
            String name,
            String phone,
            String street,
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
            String length,
            String metadata
    ) {}
}