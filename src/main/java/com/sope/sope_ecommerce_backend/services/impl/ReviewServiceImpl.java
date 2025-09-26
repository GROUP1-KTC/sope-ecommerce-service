package com.sope.sope_ecommerce_backend.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import com.sope.sope_ecommerce_backend.client.AiService;
import com.sope.sope_ecommerce_backend.enums.Sentiment;
import com.sope.sope_ecommerce_backend.services.GeminiService;
import com.sope.sope_ecommerce_backend.services.SentimentService;
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
import com.sope.sope_ecommerce_backend.enums.OrderStatus;
import com.sope.sope_ecommerce_backend.mapper.ReviewMapper;
import com.sope.sope_ecommerce_backend.repositories.OrderItemRepository;
import com.sope.sope_ecommerce_backend.repositories.ProductVariantRepository;
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
      private final ProductVariantRepository productVariantRepository;
      private final ReviewMapper reviewMapper;
      private final OrderItemRepository orderItemRepository;
      private final Cloudinary cloudinary;
      private final SentimentService sentimentService;
      private final GeminiService geminiService;

      @Override
      @Transactional(readOnly = true)
      public List<ReviewDTO> getReviewsByUserId(UUID userId) {
            List<ReviewEntity> reviews = reviewRepository.findByAppUserId(userId);
            return reviewMapper.toDtoList(reviews);
      }

      @Override
      @Transactional(readOnly = true)
      public List<ReviewDTO> getReviewsByProductId(UUID productId) {
            List<ReviewEntity> reviews = reviewRepository.findByProductVariant_Product_ProductId(productId);
            return reviewMapper.toDtoList(reviews);
      }

      @Override
      @Transactional
      public ReviewDTO createReview(ReviewCreateDTO dto, List<MultipartFile> mediaFiles, MultipartFile videoFile) {
            UUID userId = dto.appUserId();
            UUID productVariantId = dto.productVariantId();

            boolean hasBought = orderItemRepository
                        .existsByOrder_AppUser_IdAndOrder_StatusInAndProductVariant_ProductVariantId(
                                    userId,
                                    List.of(OrderStatus.DELIVERED, OrderStatus.COMPLETED),
                                    productVariantId);

            if (!hasBought) {
                  throw new IllegalStateException("User chưa mua variant này, không thể review");
            }

            ReviewEntity review = reviewRepository
                        .findByAppUser_IdAndProductVariant_ProductVariantId(userId, productVariantId)
                        .orElseGet(() -> {
                              ReviewEntity newReview = new ReviewEntity();
                              newReview.setAppUser(appUserRepository.getReferenceById(userId));
                              newReview.setProductVariant(productVariantRepository.getReferenceById(productVariantId));
                              newReview.setCreatedAt(LocalDateTime.now());
                              return newReview;
                        });

            review.setRating(dto.rating());
            review.setContent(dto.content());
            review.setUpdatedAt(LocalDateTime.now());

            Sentiment sentiment = sentimentService.getSentiment(dto.content());
            review.setSentiment(sentiment);

            if (videoFile != null && !videoFile.isEmpty()) {
                  String videoUrl = uploadFileToCloudinary(videoFile, "video");
                  review.setVideoReviewUrl(videoUrl);
            }

            // 5. Upload ảnh (nếu có)
            if (mediaFiles != null && !mediaFiles.isEmpty()) {
                  int base = review.getMediaList() == null ? 0 : review.getMediaList().size();
                  AtomicInteger priority = new AtomicInteger(base + 1);

                  for (MultipartFile file : mediaFiles) {
                        String imageUrl = uploadFileToCloudinary(file, "image");

                        ReviewMediaEntity media = new ReviewMediaEntity();
                        media.setUrl(imageUrl);
                        media.setPriority(priority.getAndIncrement());
                        media.setReview(review);

                        review.getMediaList().add(media);
                  }
            }

            ReviewEntity saved = reviewRepository.save(review);

            geminiService.generateAndSaveOverallReview(saved.getProductVariant().getProduct().getProductId());

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

            if (!review.getAppUser().getId().equals(currentUserId)) {
                  throw new AccessDeniedException("Bạn không có quyền sửa review này");
            }

            reviewMapper.updateReviewMapper(dto, review);
            review.setUpdatedAt(LocalDateTime.now());

            if (dto.imageUrlsToKeep() != null) {
                  review.getMediaList().removeIf(media -> !dto.imageUrlsToKeep().contains(media.getUrl()));
            }

            if (newFiles != null && !newFiles.isEmpty()) {
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
