package com.sope.sope_ecommerce_backend.controllers;


import java.util.List;
import java.util.UUID;

import com.sope.sope_ecommerce_backend.dto.request.ComplaintRequestDTO;
import com.sope.sope_ecommerce_backend.entities.ComplaintEntity;
import com.sope.sope_ecommerce_backend.services.ComplaintService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/complaints")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;

    @PostMapping
    public ResponseEntity<ComplaintEntity> create(@RequestBody @Valid ComplaintRequestDTO request) {
        ComplaintEntity created = complaintService.createComplaint(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<ComplaintEntity>> getAll() {
        return ResponseEntity.ok(complaintService.getAllComplaints());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplaintEntity> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(complaintService.getComplaintById(id));
    }
}
