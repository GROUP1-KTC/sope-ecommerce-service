package com.sope.sope_ecommerce_backend.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sope.sope_ecommerce_backend.dto.request.ComplaintRequest;
import com.sope.sope_ecommerce_backend.dto.response.ComplaintResponse;
import com.sope.sope_ecommerce_backend.services.ComplaintService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/complaints")
@RequiredArgsConstructor
public class ComplaintController {

	private final ComplaintService complaintService;

	@PostMapping
	public ResponseEntity<ComplaintResponse> createComplaint(@RequestBody ComplaintRequest request) {
		ComplaintResponse response = complaintService.createComplaint(request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/all")
	public ResponseEntity<List<ComplaintResponse>> getAllComplaints() {
		List<ComplaintResponse> responses = complaintService.getAllComplaints();
		return ResponseEntity.ok(responses);
	}

}