package com.sope.sope_ecommerce_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sope.sope_ecommerce_backend.dto.response.ServiceProgramDTO;
import com.sope.sope_ecommerce_backend.services.ServiceProgramConfigService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/service-program-config")
public class ServiceProgramConfigController {

	private final ServiceProgramConfigService service;

	@PatchMapping
	public ResponseEntity<ServiceProgramDTO> patchConfig(@RequestBody ServiceProgramDTO dto) {
		return ResponseEntity.ok(service.patchConfig(dto));
	}
}
