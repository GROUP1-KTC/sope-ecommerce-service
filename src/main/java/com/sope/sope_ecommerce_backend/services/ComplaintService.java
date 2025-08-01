package com.sope.sope_ecommerce_backend.services;

import java.util.List;
import java.util.UUID;

import com.sope.sope_ecommerce_backend.dto.request.ComplaintRequestDTO;
import com.sope.sope_ecommerce_backend.entities.ComplaintEntity;

public interface ComplaintService {

      ComplaintEntity createComplaint(ComplaintRequestDTO complaint);

      ComplaintEntity getComplaintById(UUID id);

      List<ComplaintEntity> getAllComplaints();
}