package com.sope.sope_ecommerce_backend.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderItemResponse(

                UUID productVariantId,
                String productName,
                int quantity,
                BigDecimal price,
                String imageUrl,
                String slug,
                BigDecimal commissionFeePercent,
                List<AttributeDTO> attributes) {
}
