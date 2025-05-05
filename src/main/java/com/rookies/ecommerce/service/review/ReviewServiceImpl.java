package com.rookies.ecommerce.service.review;

import com.rookies.ecommerce.dto.request.CreateReviewRequest;
import com.rookies.ecommerce.dto.request.UpdateReviewRequest;
import com.rookies.ecommerce.dto.response.*;
import com.rookies.ecommerce.entity.Product;
import com.rookies.ecommerce.entity.Review;
import com.rookies.ecommerce.entity.User;
import com.rookies.ecommerce.exception.AppException;
import com.rookies.ecommerce.exception.ErrorCode;
import com.rookies.ecommerce.mapper.ReviewMapper;
import com.rookies.ecommerce.repository.ReviewRepository;
import com.rookies.ecommerce.service.product.ProductService;
import com.rookies.ecommerce.service.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewServiceImpl implements ReviewService {

    ReviewRepository reviewRepository;

    ReviewMapper reviewMapper;

    UserService userService;

    ProductService productService;

    @Override
    public void createReview(CreateReviewRequest request) {
        try {
            User user = userService.getUserFromToken();
            Product product = productService.getProductEntityById(request.getProductId());
            Review review = reviewMapper.toReview(request);

            review.setProduct(product);
            review.setCustomer(user.getCustomer());
            reviewRepository.save(review);
        }
        catch (DataIntegrityViolationException ex) {
            throw new AppException(ErrorCode.ALREADY_REVIEWD);
        }

    }

    @Override
    public void updateReview(String reviewId, UpdateReviewRequest request) {
        Review review = reviewRepository.findById(UUID.fromString(reviewId))
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!review.getCustomer().equals(userService.getUserFromToken().getCustomer())) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        reviewMapper.updateReview(request, review);
        reviewRepository.save(review);
    }

    @Override
    public Page<ReviewResponse> getReviewByProductId(UUID productId, int page, int size, String sortBy, String sortDir) {
        Product product = productService.getProductEntityById(productId);
        return reviewRepository.findAllByProduct(product,
                        PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy)))
                .map(reviewMapper::toReviewResponse);
    }

    @Override
    public ReviewStatistic getReviewStatisticByProductId(UUID id) {
        Product product = productService.getProductEntityById(id);
        long count = reviewRepository.countByProduct(product);
        List<RatingCount> ratingCounts = new ArrayList<>();
        for (int i = 5; i >= 1; i--) {
            RatingCount ratingCount = RatingCount.builder()
                    .rating(i)
                    .count(reviewRepository.countByProductAndRating(product, i))
                    .build();
            ratingCount.setPercent(count != 0 ? (double) ratingCount.getCount() / count * 100 : 0);
            ratingCounts.add(ratingCount);
        }
        return ReviewStatistic.builder()
                .count(count)
                .averageRating(count != 0 ? reviewRepository.getAvgRatingByProduct(product) : 0)
                .ratingCounts(ratingCounts)
                .build();
    }

    @Override
    public void deleteReview(UUID id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        reviewRepository.delete(review);
    }

    @Override
    public ReviewDetailResponse getReviewDetailById(UUID id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        return reviewMapper.toReviewDetail(review);
    }

    @Override
    public Page<UserReviewResponse> getReviewByUser(int page, int size, String sortBy, String sortDir) {
        User user = userService.getUserFromToken();
        return reviewRepository.findAllByCustomer(user.getCustomer(),
                        PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy)))
                .map(reviewMapper::toUserReviewResponse);
    }

    @Override
    public Page<AllReviewResponse> getAllReview(int page, int size, String sortBy, String sortDir) {
        return reviewRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy)))
                .map(reviewMapper::toAllReviewResponse);
    }
}
