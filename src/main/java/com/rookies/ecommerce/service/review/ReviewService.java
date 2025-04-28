package com.rookies.ecommerce.service.review;

import com.rookies.ecommerce.dto.request.CreateReviewRequest;
import com.rookies.ecommerce.dto.request.UpdateReviewRequest;
import com.rookies.ecommerce.dto.response.*;
import org.springframework.data.domain.Page;

import java.util.UUID;

/**
 * Service interface for handling review-related operations.
 */
public interface ReviewService {

    /**
     * Creates a new review.
     *
     * @param request the request object containing review details
     */
    void createReview(CreateReviewRequest request);

    /**
     * Updates an existing review.
     *
     * @param reviewId the unique identifier of the review to update
     * @param request the request object containing updated review details
     */
    void updateReview(String reviewId, UpdateReviewRequest request);

    /**
     * Retrieves a paginated list of reviews for a specific product.
     *
     * @param productId the unique identifier of the product
     * @param page the page number to retrieve
     * @param size the number of reviews per page
     * @param sortBy the field to sort the reviews by
     * @param sortDir the direction of sorting (asc/desc)
     * @return a paginated list of {@link ReviewResponse} objects
     */
    Page<ReviewResponse> getReviewByProductId(UUID productId, int page, int size, String sortBy, String sortDir);

    /**
     * Retrieves review statistics for a specific product.
     *
     * @param id the unique identifier of the product
     * @return a {@link ReviewStatistic} object containing review statistics
     */
    ReviewStatistic getReviewStatisticByProductId(UUID id);

    /**
     * Deletes a review by its unique identifier.
     *
     * @param id the unique identifier of the review to delete
     */
    void deleteReview(UUID id);

    /**
     * Retrieves the details of a review by its unique identifier.
     *
     * @param id the unique identifier of the review
     * @return a {@link ReviewDetailResponse} containing the review details
     */
    ReviewDetailResponse getReviewDetailById(UUID id);

    /**
     * Retrieves a paginated list of reviews created by the currently authenticated user.
     *
     * @param page the page number to retrieve
     * @param size the number of reviews per page
     * @param sortBy the field to sort the reviews by
     * @param sortDir the direction of sorting (asc/desc)
     * @return a paginated list of {@link UserReviewResponse} objects
     */
    Page<UserReviewResponse> getReviewByUser(int page, int size, String sortBy, String sortDir);

    /**
     * Retrieves a paginated list of all reviews.
     *
     * @param page the page number to retrieve
     * @param size the number of reviews per page
     * @param sortBy the field to sort the reviews by
     * @param sortDir the direction of sorting (asc/desc)
     * @return a paginated list of {@link AllReviewResponse} objects
     */
    Page<AllReviewResponse> getAllReview(int page, int size, String sortBy, String sortDir);

}