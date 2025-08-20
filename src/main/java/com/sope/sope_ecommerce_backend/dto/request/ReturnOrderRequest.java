package com.sope.sope_ecommerce_backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnOrderRequest {
      private UUID orderId;
      private String reason;
      // private List<ReturnItemRequest> items;
}
