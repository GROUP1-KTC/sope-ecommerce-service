package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.request.AdsProgramRequest;
import com.sope.sope_ecommerce_backend.dto.request.ChangeStatusRequest;
import com.sope.sope_ecommerce_backend.dto.request.FlashSaleProgramRequest;
import com.sope.sope_ecommerce_backend.dto.response.AdsProgramDTO;
import com.sope.sope_ecommerce_backend.dto.response.FlashSaleProgramDTO;
import com.sope.sope_ecommerce_backend.enums.ProgramStatus;
import com.sope.sope_ecommerce_backend.services.ServiceProgramService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/service-programs")
@RequiredArgsConstructor
public class ServiceProgramController {

	private final ServiceProgramService service;

	// ---------------- Ads ----------------
	@GetMapping("/ads/active")
	public ResponseEntity<List<AdsProgramDTO>> getActiveAds() {
		return ResponseEntity.ok(service.getActiveAdsPrograms());
	}

	@PostMapping("/ads")
	public ResponseEntity<AdsProgramDTO> createAds(@RequestBody AdsProgramRequest request) {
		return ResponseEntity.ok(service.createAdsProgram(request));
	}

	@PatchMapping("/ads/{id}/status")
	public ResponseEntity<AdsProgramDTO> changeAdsStatus(
			@PathVariable UUID id,
			@RequestBody ChangeStatusRequest request) {
		if (request.status() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Action is required");
		}
		return ResponseEntity.ok(service.changeAdsStatus(id, request.status()));
	}

	// ---------------- FlashSale ----------------
	@GetMapping("/flash/active")
	public ResponseEntity<List<FlashSaleProgramDTO>> getActiveFlashSales() {
		return ResponseEntity.ok(service.getActiveFlashSales());
	}

	@PostMapping("/flash")
	public ResponseEntity<FlashSaleProgramDTO> createFlashSale(@RequestBody FlashSaleProgramRequest request) {
		return ResponseEntity.ok(service.createFlashSale(request));
	}

	@PatchMapping("/flash/{id}/status")
	public ResponseEntity<FlashSaleProgramDTO> changeFlashStatus(
			@PathVariable UUID id,
			@RequestBody ProgramStatus request) {
		if (request == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Action is required");
		}

		return ResponseEntity.ok(service.changeFlashStatus(id, request));
	}
}
