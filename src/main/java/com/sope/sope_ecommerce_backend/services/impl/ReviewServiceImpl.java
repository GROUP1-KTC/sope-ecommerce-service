package com.sope.sope_ecommerce_backend.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sope.sope_ecommerce_backend.dto.request.ReviewCreateDTO;
import com.sope.sope_ecommerce_backend.dto.request.ReviewUpdateDTO;
import com.sope.sope_ecommerce_backend.dto.response.ReviewDTO;
import com.sope.sope_ecommerce_backend.entities.ReviewEntity;
import com.sope.sope_ecommerce_backend.entities.ReviewMediaEntity;
import com.sope.sope_ecommerce_backend.enums.MediaType;
import com.sope.sope_ecommerce_backend.enums.OrderStatus;
import com.sope.sope_ecommerce_backend.mapper.ReviewMapper;
import com.sope.sope_ecommerce_backend.repositories.OrderItemRepository;
import com.sope.sope_ecommerce_backend.repositories.ProductRepository;
import com.sope.sope_ecommerce_backend.repositories.ReviewRepository;
import com.sope.sope_ecommerce_backend.repositories.UserRepository;
import com.sope.sope_ecommerce_backend.security.user.CustomUserDetails;
import com.sope.sope_ecommerce_backend.services.ReviewService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
      private final ReviewRepository reviewRepository;
      private final UserRepository appUserRepository;
      private final ProductRepository productRepository;
      private final ReviewMapper reviewMapper;
      private final OrderItemRepository orderItemRepository;
      private final Cloudinary cloudinary;

      @Override
      @Transactional(readOnly = true)
      public List<ReviewDTO> getReviewsByProductId(UUID productId) {
            List<ReviewEntity> reviews = reviewRepository.findByProduct_ProductId(productId);
            return reviewMapper.toDtoList(reviews);
      }

      @Override
      @Transactional
      public ReviewDTO createReview(ReviewCreateDTO dto, List<MultipartFile> mediaFiles) {
            UUID userId = dto.appUserId();
            UUID productId = dto.productId();

            boolean hasBought = orderItemRepository
                        .existsByOrder_AppUser_IdAndOrder_StatusInAndProductVariant_Product_ProductId(
                                    userId,
                                    List.of(OrderStatus.DELIVERED, OrderStatus.COMPLETED),
                                    productId);

            if (!hasBought) {
                  throw new IllegalStateException("User chưa mua sản phẩm này, không thể review");
            }

            // 2. Kiểm tra xem review đã tồn tại chưa (mỗi user chỉ 1 review cho 1 product)
            ReviewEntity review = reviewRepository
                        .findByAppUser_IdAndProduct_ProductId(userId, productId)
                        .orElseGet(() -> {
                              ReviewEntity newReview = new ReviewEntity();
                              newReview.setAppUser(appUserRepository.getReferenceById(userId));
                              newReview.setProduct(productRepository.getReferenceById(productId));
                              newReview.setCreatedAt(LocalDateTime.now());
                              return newReview;
                        });

            // 3. Cập nhật nội dung review
            review.setRating(dto.rating());
            review.setContent(dto.content());
            review.setUpdatedAt(LocalDateTime.now());

            // 4. Upload media (nếu có)
            if (mediaFiles != null && !mediaFiles.isEmpty()) {
                  AtomicInteger priority = new AtomicInteger(1);

                  review.setMediaList(
                              mediaFiles.stream()
                                          .map(file -> {
                                                String type = file.getContentType() != null
                                                            && file.getContentType().startsWith("video")
                                                                        ? "video"
                                                                        : "image";

                                                String url = uploadFileToCloudinary(file, type);

                                                ReviewMediaEntity media = new ReviewMediaEntity();
                                                media.setUrl(url);
                                                media.setType("video".equals(type) ? MediaType.VIDEO : MediaType.IMAGE);
                                                media.setPriority(priority.getAndIncrement()); // giống productImages
                                                media.setReview(review);

                                                return media;
                                          })
                                          .collect(Collectors.toList()));
            }

            ReviewEntity saved = reviewRepository.save(review);
            return reviewMapper.toDto(saved);
      }

      @Override
      @Transactional
      public ReviewDTO updateReview(UUID reviewId, ReviewUpdateDTO dto,
                  List<MultipartFile> newFiles) {
            ReviewEntity review = reviewRepository.findById(reviewId)
                        .orElseThrow(() -> new EntityNotFoundException("Review not found"));

            UUID currentUserId = ((CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal()).getUserId();

            // 1. Check quyền sở hữu
            if (!review.getAppUser().getId().equals(currentUserId)) {
                  throw new AccessDeniedException("Bạn không có quyền sửa review này");
            }

            // 2. Update nội dung
            reviewMapper.updateReviewMapper(dto, review);
            review.setUpdatedAt(LocalDateTime.now());

            // 3. Giữ lại ảnh cũ nếu có
            if (dto.imageUrlsToKeep() != null) {
                  review.getMediaList().removeIf(media -> !dto.imageUrlsToKeep().contains(media.getUrl()));
            }

            // 4. Thêm ảnh/video mới
            if (newFiles != null && !newFiles.isEmpty()) {
                  // giống cách anh làm productImages
            }

            ReviewEntity saved = reviewRepository.save(review);
            return reviewMapper.toDto(saved);
      }

      private String uploadFileToCloudinary(MultipartFile file, String type) {
            if (file != null && !file.isEmpty()) {
                  try {
                        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                                    "video".equals(type) ? ObjectUtils.asMap("resource_type", "video")
                                                : ObjectUtils.emptyMap());
                        return (String) uploadResult.get("secure_url");
                  } catch (Exception e) {
                        throw new RuntimeException("Failed to upload " + type, e);
                  }
            }
            return null;
      }
}
