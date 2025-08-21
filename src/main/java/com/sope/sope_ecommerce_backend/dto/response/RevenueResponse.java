package com.sope.sope_ecommerce_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class RevenueResponse {
      private BigDecimal revenue; // doanh thu (shop hoặc admin)
}
