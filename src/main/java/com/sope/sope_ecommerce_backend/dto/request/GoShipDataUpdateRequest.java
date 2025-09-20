package com.sope.sope_ecommerce_backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoShipDataUpdateRequest(
        @JsonProperty("gcode") String gcode,
        @JsonProperty("code") String code,
        @JsonProperty("order_id") String orderId,
        @JsonProperty("weight") String weight,
        @JsonProperty("fee") String fee,
        @JsonProperty("cod") String cod,
        @JsonProperty("payer") String payer,
        @JsonProperty("status") String status,
        @JsonProperty("status_text") String statusText,
        @JsonProperty("message") String message,
        @JsonProperty("tracking_url") String trackingUrl,
        @JsonProperty("description") String description,
        @JsonProperty("sorting_code") String sortingCode,
        @JsonProperty("return_sorting_code") String returnSortingCode,
        @JsonProperty("is_return") int isReturn,
        @JsonProperty("is_part_delivery") int isPartDelivery,
        @JsonProperty("is_lost") int isLost
) {
}
