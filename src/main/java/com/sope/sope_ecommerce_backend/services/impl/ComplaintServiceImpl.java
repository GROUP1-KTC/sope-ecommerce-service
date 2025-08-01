package com.sope.sope_ecommerce_backend.services.impl;



import com.sope.sope_ecommerce_backend.dto.request.ComplaintRequestDTO;
import com.sope.sope_ecommerce_backend.entities.ComplaintEntity;
import com.sope.sope_ecommerce_backend.entities.UserEntity;
import com.sope.sope_ecommerce_backend.enums.ComplaintStatus;
import com.sope.sope_ecommerce_backend.repositories.ComplaintRepository;
import com.sope.sope_ecommerce_backend.repositories.OrderRepository;
import com.sope.sope_ecommerce_backend.repositories.ShopRepository;
import com.sope.sope_ecommerce_backend.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import com.sope.sope_ecommerce_backend.services.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComplaintServiceImpl implements ComplaintService {
    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final OrderRepository orderRepository;

    @Override
    public ComplaintEntity createComplaint(ComplaintRequestDTO request) {
        UserEntity user = userRepository.findById(request.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        switch (request.targetType()) {
            case CUSTOMER -> {
                // Target là User
                if (!userRepository.existsById(request.targetId())) {
                    throw new EntityNotFoundException("Target user not found");
                }
            }

            case SHOP -> {
                if (!shopRepository.existsById(request.targetId())) {
                    throw new EntityNotFoundException("Target shop not found");
                }
            }

            case PRODUCT_ORDER -> {
                if (!orderRepository.existsById(request.targetId())) {
                    throw new EntityNotFoundException("Target order not found");
                }
            }

            default -> throw new IllegalArgumentException("Unsupported target type: " + request.targetType());
        }

        ComplaintEntity complaint = ComplaintEntity.builder()
                .user(user)
                .targetId(request.targetId())
                .targetType(request.targetType())
                .description(request.description())
                .status(ComplaintStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        return complaintRepository.save(complaint);
    }

    @Override
    public ComplaintEntity getComplaintById(UUID id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found"));
    }

    @Override
    public List<ComplaintEntity> getAllComplaints() {
        return complaintRepository.findAll();
    }
}
