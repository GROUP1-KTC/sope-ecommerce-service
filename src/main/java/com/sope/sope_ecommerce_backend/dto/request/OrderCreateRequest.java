package com.sope.sope_ecommerce_backend.dto.request;

//public record OrderCreateRequest(
//        UUID shippingAddressId,
//        BigDecimal shippingCharge,
//        String note,
//        List<String> discountCodes,
//        String idempotencyKey
//) {
//}

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "orderType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UserOrderCreateRequest.class, name = "user"),
        @JsonSubTypes.Type(value = GuestOrderCreateRequest.class, name = "guest")
})
public sealed interface OrderCreateRequest permits UserOrderCreateRequest, GuestOrderCreateRequest {
    String idempotencyKey();
}
