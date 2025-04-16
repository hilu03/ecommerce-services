package com.rookies.ecommerce.service.review;

import com.rookies.ecommerce.dto.request.CreateReviewRequest;
import com.rookies.ecommerce.dto.request.UpdateReviewRequest;
import com.rookies.ecommerce.dto.response.*;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ReviewService {

    void createReview(CreateReviewRequest request);

    void updateReview(String reviewId, UpdateReviewRequest request);

    Page<ReviewResponse> getReviewByProductId(UUID productId, int page, int size, String sortBy, String sortDir);

    ReviewStatistic getReviewStatisticByProductId(UUID id);

    void deleteReview(UUID id);

    ReviewDetailResponse getReviewDetailById(UUID id);

    Page<UserReviewResponse> getReviewByUser(int page, int size, String sortBy, String sortDir);

    Page<AllReviewResponse> getAllReview(int page, int size, String sortBy, String sortDir);

}
