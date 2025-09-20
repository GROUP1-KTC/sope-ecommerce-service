package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.request.AdsProgramRequest;
import com.sope.sope_ecommerce_backend.dto.request.FlashSaleProgramRequest;
import com.sope.sope_ecommerce_backend.dto.response.AdsProgramDTO;
import com.sope.sope_ecommerce_backend.dto.response.FlashSaleProgramDTO;
import com.sope.sope_ecommerce_backend.enums.ProgramStatus;

import java.util.List;
import java.util.UUID;

public interface ServiceProgramService {
  // ---------------- Ads ----------------
  AdsProgramDTO createAdsProgram(AdsProgramRequest request);

  List<AdsProgramDTO> getActiveAdsPrograms();

  AdsProgramDTO changeAdsStatus(UUID id, ProgramStatus request);

  // ---------------- FlashSale ----------------
  FlashSaleProgramDTO createFlashSale(FlashSaleProgramRequest request);

  List<FlashSaleProgramDTO> getActiveFlashSales();

  FlashSaleProgramDTO changeFlashStatus(UUID id, ProgramStatus request);
}