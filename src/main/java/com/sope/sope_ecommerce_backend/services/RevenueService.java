package com.sope.sope_ecommerce_backend.services;

import java.util.UUID;

import com.sope.sope_ecommerce_backend.dto.response.RevenueResponse;

public interface RevenueService {
      RevenueResponse calculateShopRevenue(UUID shopId);

      RevenueResponse calculateAdminRevenue();
}
