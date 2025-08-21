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

@Schema(
        oneOf = { GuestOrderCreateRequest.class, UserOrderCreateRequest.class },
        discriminatorProperty = "orderType",
        discriminatorMapping = {
                @DiscriminatorMapping(value = "guest", schema = GuestOrderCreateRequest.class),
                @DiscriminatorMapping(value = "user", schema = UserOrderCreateRequest.class)
        }
)
public sealed interface OrderCreateRequest permits GuestOrderCreateRequest, UserOrderCreateRequest {
    String idempotencyKey();
}
