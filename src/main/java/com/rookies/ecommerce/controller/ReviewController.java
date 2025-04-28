package com.rookies.ecommerce.controller;

import com.rookies.ecommerce.constant.MessageResponse;
import com.rookies.ecommerce.dto.request.CreateReviewRequest;
import com.rookies.ecommerce.dto.request.UpdateReviewRequest;
import com.rookies.ecommerce.dto.response.APIResponse;
import com.rookies.ecommerce.service.review.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller for managing and moderating reviews.
 */
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/reviews")
@Tag(name = "Reviews", description = "APIs for managing and moderating reviews")
public class ReviewController {

    ReviewService reviewService;

    /**
     * Retrieves a paginated list of all reviews.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of reviews per page (default is 10)
     * @param sortBy the field to sort the reviews by (default is "createdAt")
     * @param sortDir the direction of sorting (asc for ascending, desc for descending; default is "asc")
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the list of reviews
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<APIResponse> getAllReview(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "createdAt") String sortBy,
                                                    @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                reviewService.getAllReview(page, size, sortBy, sortDir)));
    }

    /**
     * Retrieves the details of a review by its unique identifier.
     *
     * @param id the unique identifier of the review
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the review details
     */
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getReviewDetailById(@PathVariable UUID id) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                reviewService.getReviewDetailById(id)));
    }

    /**
     * Retrieves a paginated list of reviews created by the currently authenticated user.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of reviews per page (default is 10)
     * @param sortBy the field to sort the reviews by (default is "createdAt")
     * @param sortDir the direction of sorting (asc for ascending, desc for descending; default is "asc")
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the user's reviews
     */
    @GetMapping("/user")
    public ResponseEntity<APIResponse> getReviewByUser(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(defaultValue = "createdAt") String sortBy,
                                                       @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                reviewService.getReviewByUser(page, size, sortBy, sortDir)));
    }

    /**
     * Retrieves review statistics for a specific product by its unique identifier.
     *
     * @param id the unique identifier of the product
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the review statistics
     */
    @GetMapping("/product/{id}/statistic")
    public ResponseEntity<APIResponse> getReviewStatisticByProductId(@PathVariable UUID id) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                reviewService.getReviewStatisticByProductId(id)));
    }

    /**
     * Retrieves a paginated list of reviews for a specific product by its unique identifier.
     *
     * @param id the unique identifier of the product
     * @param page the page number to retrieve (default is 0)
     * @param size the number of reviews per page (default is 10)
     * @param sortBy the field to sort the reviews by (default is "createdAt")
     * @param sortDir the direction of sorting (asc for ascending, desc for descending; default is "asc")
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the product's reviews
     */
    @GetMapping("/product/{id}")
    public ResponseEntity<APIResponse> getReviewByProductId(@PathVariable UUID id,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(defaultValue = "createdAt") String sortBy,
                                                            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                reviewService.getReviewByProductId(id, page, size, sortBy, sortDir)));
    }

    /**
     * Creates a new review.
     *
     * @param request the request object containing the review details
     * @return a {@link ResponseEntity} containing an {@link APIResponse} indicating the creation status
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping()
    public ResponseEntity<APIResponse> createReview(@RequestBody @Valid CreateReviewRequest request) {
        reviewService.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse(MessageResponse.CREATED_SUCCESSFULLY, null));
    }

    /**
     * Updates an existing review by its unique identifier.
     *
     * @param request the request object containing the updated review details
     * @param id the unique identifier of the review
     * @return a {@link ResponseEntity} containing an {@link APIResponse} indicating the update status
     */
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse> updateReview(@RequestBody @Valid UpdateReviewRequest request,
                                                    @PathVariable String id) {
        reviewService.updateReview(id, request);
        return ResponseEntity.ok(new APIResponse(MessageResponse.UPDATED_SUCCESSFULLY, null));
    }

    /**
     * Deletes a review by its unique identifier.
     *
     * @param id the unique identifier of the review
     * @return a {@link ResponseEntity} containing an {@link APIResponse} indicating the deletion status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> deleteReview(@PathVariable UUID id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok(new APIResponse(MessageResponse.UPDATED_SUCCESSFULLY, null));
    }

}