package com.rookies.ecommerce.controller;

import com.rookies.ecommerce.constant.MessageResponse;
import com.rookies.ecommerce.dto.request.CreateReviewRequest;
import com.rookies.ecommerce.dto.request.UpdateReviewRequest;
import com.rookies.ecommerce.dto.response.APIResponse;
import com.rookies.ecommerce.service.review.ReviewService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/reviews")
public class ReviewController {

    ReviewService reviewService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<APIResponse> getAllReview(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "createdAt") String sortBy,
                                                    @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                reviewService.getAllReview(page, size, sortBy, sortDir)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getReviewDetailById(@PathVariable UUID id) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                reviewService.getReviewDetailById(id)));
    }

    @GetMapping("/user")
    public ResponseEntity<APIResponse> getReviewByUser(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(defaultValue = "createdAt") String sortBy,
                                                       @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                reviewService.getReviewByUser(page, size, sortBy, sortDir)));
    }

    @GetMapping("/product/{id}/statistic")
    public ResponseEntity<APIResponse> getReviewStatisticByProductId(@PathVariable UUID id) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                reviewService.getReviewStatisticByProductId(id)));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<APIResponse> getReviewByProductId(@PathVariable UUID id,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(defaultValue = "createdAt") String sortBy,
                                                            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                reviewService.getReviewByProductId(id, page, size, sortBy, sortDir)));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping()
    public ResponseEntity<APIResponse> createReview(@RequestBody @Valid CreateReviewRequest request) {
        reviewService.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse(MessageResponse.CREATED_SUCCESSFULLY, null));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse> updateReview(@RequestBody @Valid UpdateReviewRequest request,
                                                    @PathVariable String id) {
        reviewService.updateReview(id, request);
        return ResponseEntity.ok(new APIResponse(MessageResponse.UPDATED_SUCCESSFULLY, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> deleteReview(@PathVariable UUID id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok(new APIResponse(MessageResponse.UPDATED_SUCCESSFULLY, null));
    }

}
