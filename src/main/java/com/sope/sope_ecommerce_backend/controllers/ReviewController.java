package com.sope.sope_ecommerce_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sope.sope_ecommerce_backend.dto.request.ReviewCreateDTO;
import com.sope.sope_ecommerce_backend.dto.request.ReviewUpdateDTO;
import com.sope.sope_ecommerce_backend.dto.response.ReviewDTO;
import com.sope.sope_ecommerce_backend.services.ReviewService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

      private final ReviewService reviewService;

      @GetMapping("/user/{userId}")
      public ResponseEntity<List<ReviewDTO>> getReviewsByUserId(@PathVariable UUID userId) {
            List<ReviewDTO> reviews = reviewService.getReviewsByUserId(userId);
            return ResponseEntity.ok(reviews);
      }

      @GetMapping("/{productId}")
      public ResponseEntity<List<ReviewDTO>> getReviewsByProductId(@PathVariable UUID productId) {
            List<ReviewDTO> reviews = reviewService.getReviewsByProductId(productId);
            return ResponseEntity.ok(reviews);
      }

      @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
      public ResponseEntity<ReviewDTO> createReview(
                  @RequestPart("review") ReviewCreateDTO dto,
                  @RequestPart(value = "mediaFiles", required = false) List<MultipartFile> mediaFiles,
                  @RequestPart(value = "videoFile", required = false) MultipartFile videoFile) {

            ReviewDTO review = reviewService.createReview(dto, mediaFiles, videoFile);
            return ResponseEntity.ok(review);
      }

      @PatchMapping(value = "/{reviewId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
      public ResponseEntity<ReviewDTO> updateProduct(
                  @PathVariable UUID reviewId,
                  @RequestPart("review") ReviewUpdateDTO reviewUpdateDTO,
                  @RequestPart(value = "mediaFiles", required = false) List<MultipartFile> mediaFiles) {
            ReviewDTO updateReview = reviewService.updateReview(reviewId,
                        reviewUpdateDTO, mediaFiles);

            return ResponseEntity.ok(updateReview);
      }

}
