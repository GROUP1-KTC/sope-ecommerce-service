package com.sope.sope_ecommerce_backend.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.sope.sope_ecommerce_backend.dto.request.ComplaintRequest;
import com.sope.sope_ecommerce_backend.dto.response.ComplaintResponse;
import com.sope.sope_ecommerce_backend.entities.AppUser;
import com.sope.sope_ecommerce_backend.entities.ComplaintEntity;
import com.sope.sope_ecommerce_backend.enums.ComplaintStatus;
import com.sope.sope_ecommerce_backend.mapper.ComplaintMapper;
import com.sope.sope_ecommerce_backend.repositories.UserRepository;
import com.sope.sope_ecommerce_backend.security.user.CustomUserDetails;
import com.sope.sope_ecommerce_backend.repositories.ComplaintRepository;
import com.sope.sope_ecommerce_backend.services.ComplaintService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComplaintServiceImpl implements ComplaintService {
	private final ComplaintRepository complaintRepository;
	private final ComplaintMapper complaintMapper;
	private final UserRepository appUserRepository;

	@Override
	@Transactional
	public ComplaintResponse createComplaint(ComplaintRequest request) {

		UUID currentUserId = ((CustomUserDetails) SecurityContextHolder
				.getContext()
				.getAuthentication()
				.getPrincipal()).getUserId();

		AppUser reporter = appUserRepository.findById(currentUserId)
				.orElseThrow(() -> new RuntimeException("User not found"));

		ComplaintEntity entity = complaintMapper.toEntity(request);

		entity.setReporter(reporter);
		entity.setStatusComplaint(ComplaintStatus.PENDING);
		entity.setCreatedAt(LocalDateTime.now());

		ComplaintEntity saved = complaintRepository.save(entity);

		return complaintMapper.toResponse(saved);
	}

	private boolean isAdmin(CustomUserDetails currentUser) {
		return currentUser.getAuthorities().stream()
				.anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")
						|| auth.getAuthority().equals("ADMIN"));
	}

	@Override
	@Transactional(readOnly = true)
	public List<ComplaintResponse> getAllComplaints() {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!(principal instanceof CustomUserDetails currentUser)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xem complaint này");
		}

		if (!isAdmin(currentUser)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xem complaint này");
		}

		List<ComplaintEntity> complaints = complaintRepository.findAll();
		return complaintMapper.toDtoList(complaints);
	}

}
