package com.sope.sope_ecommerce_backend.dto.request;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnItemRequest {
      private UUID productVariantId;
      private String reasonDetail;
      private BigDecimal refundAmountPerItem;
      private int quantity;
}
