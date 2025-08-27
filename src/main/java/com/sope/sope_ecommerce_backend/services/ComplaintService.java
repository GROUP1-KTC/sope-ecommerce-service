package com.sope.sope_ecommerce_backend.services;

import java.util.List;
import java.util.UUID;

import com.sope.sope_ecommerce_backend.dto.request.ComplaintRequest;
import com.sope.sope_ecommerce_backend.dto.response.ComplaintResponse;

public interface ComplaintService {
  ComplaintResponse createComplaint(ComplaintRequest request);

  List<ComplaintResponse> getAllComplaints();
}