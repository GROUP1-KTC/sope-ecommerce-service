// package com.sope.sope_ecommerce_backend.services.impl;

// import java.util.List;
// import java.util.UUID;

// import org.springframework.stereotype.Service;

// import com.sope.sope_ecommerce_backend.dto.request.ReviewCreateDTO;
// import com.sope.sope_ecommerce_backend.dto.response.ReviewDTO;
// import com.sope.sope_ecommerce_backend.entities.AppUser;
// import com.sope.sope_ecommerce_backend.entities.ProductVariant;
// import com.sope.sope_ecommerce_backend.entities.ReviewEntity;
// import com.sope.sope_ecommerce_backend.enums.OrderStatus;
// import com.sope.sope_ecommerce_backend.mapper.ReviewMapper;
// import com.sope.sope_ecommerce_backend.repositories.OrderItemRepository;
// import com.sope.sope_ecommerce_backend.repositories.ProductVariantRepository;
// import com.sope.sope_ecommerce_backend.repositories.ReviewRepository;
// import com.sope.sope_ecommerce_backend.repositories.UserRepository;
// import com.sope.sope_ecommerce_backend.services.ReviewService;

// import jakarta.persistence.EntityNotFoundException;

// import org.springframework.transaction.annotation.Transactional;
// import lombok.RequiredArgsConstructor;

// @Service
// @RequiredArgsConstructor
// public class ReviewServiceImpl implements ReviewService {
// private final ReviewRepository reviewRepository;
// private final UserRepository appUserRepository;
// private final ProductVariantRepository productVariantRepository;
// private final ReviewMapper reviewMapper;
// private final OrderItemRepository orderItemRepository;

// @Transactional
// public ReviewDTO createReview(ReviewCreateDTO dto) {

// boolean hasPurchased =
// orderItemRepository.existsByUserAndStatusAndProductVariant(
// dto.getAppuserId(),
// OrderStatus.COMPLETED,
// dto.getProductVariantId());

// if (!hasPurchased) {
// throw new IllegalStateException("Bạn chỉ có thể review sản phẩm đã mua thành
// công.");
// }

// // Check duplicate
// reviewRepository.findByAppUser_IdAndProductVariant_Id(dto.getAppuserId(),
// dto.getProductVariantId())
// .ifPresent(r -> {
// throw new IllegalStateException("User đã review sản phẩm variant này rồi");
// });

// // Load User
// AppUser user = appUserRepository.findById(dto.getAppuserId())
// .orElseThrow(() -> new EntityNotFoundException("User not found"));

// // Load ProductVariant
// ProductVariant productVariant =
// productVariantRepository.findById(dto.getProductVariantId())
// .orElseThrow(() -> new EntityNotFoundException("ProductVariant not found"));

// // Map DTO -> Entity
// ReviewEntity review = reviewMapper.toEntity(dto);

// // Set quan hệ
// review.setAppUser(user);
// review.setProductVariant(productVariant);

// // Save
// ReviewEntity saved = reviewRepository.save(review);

// return reviewMapper.toDto(saved);
// }

// @Override
// @Transactional(readOnly = true)
// public List<ReviewDTO> getReviewsByProductVariant(UUID productVariantId) {
// List<ReviewEntity> reviews = reviewRepository.findAll()
// .stream()
// .filter(r ->
// r.getProductVariant().getProductVariantId().equals(productVariantId))
// .toList();
// return reviewMapper.toDtoList(reviews);
// }
// }
