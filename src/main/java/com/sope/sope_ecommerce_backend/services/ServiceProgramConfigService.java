package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.response.ServiceProgramDTO;

public interface ServiceProgramConfigService {
  ServiceProgramDTO patchConfig(ServiceProgramDTO dto); // PATCH chỉ còn đây
}