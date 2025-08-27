package com.sope.sope_ecommerce_backend.services;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import com.sope.sope_ecommerce_backend.dto.request.ReviewCreateDTO;
import com.sope.sope_ecommerce_backend.dto.request.ReviewUpdateDTO;
import com.sope.sope_ecommerce_backend.dto.response.ReviewDTO;

public interface ReviewService {
      ReviewDTO createReview(ReviewCreateDTO dto, List<MultipartFile> mediaFiles);

      ReviewDTO updateReview(UUID reviewId, ReviewUpdateDTO dto, List<MultipartFile> newFiles);

      List<ReviewDTO> getReviewsByProductId(UUID productId);

}
