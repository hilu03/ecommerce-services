package com.rookies.ecommerce.service;

import com.rookies.ecommerce.dto.request.CreateReviewRequest;
import com.rookies.ecommerce.entity.Customer;
import com.rookies.ecommerce.entity.Product;
import com.rookies.ecommerce.entity.Review;
import com.rookies.ecommerce.entity.User;
import com.rookies.ecommerce.exception.AppException;
import com.rookies.ecommerce.exception.ErrorCode;
import com.rookies.ecommerce.mapper.ReviewMapper;
import com.rookies.ecommerce.repository.ReviewRepository;
import com.rookies.ecommerce.service.product.ProductService;
import com.rookies.ecommerce.service.review.ReviewServiceImpl;
import com.rookies.ecommerce.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceUnitTests {

    @Mock
    ReviewRepository reviewRepository;

    @Mock
    ReviewMapper reviewMapper;

    @Mock
    UserService userService;

    @Mock
    ProductService productService;

    @InjectMocks
    ReviewServiceImpl reviewService;

    @Test
    public void createReview_alreadyReviewed_throwException() {
        UUID productId = UUID.randomUUID();
        CreateReviewRequest request = CreateReviewRequest.builder()
                .productId(productId)
                .rating(5)
                .comment("this is a test review")
                .build();

        User user = new User();
        Customer customer = new Customer();
        user.setCustomer(customer);

        Product product = new Product();

        Review review = new Review();
        when(userService.getUserFromToken()).thenReturn(user);
        when(productService.getProductEntityById(productId)).thenReturn(product);
        when(reviewMapper.toReview(request)).thenReturn(review);
        when(reviewRepository.save(any(Review.class)))
                .thenThrow(new DataIntegrityViolationException("Unique constraint violation"));

        // WHEN & THEN
        AppException ex = assertThrows(AppException.class, () -> reviewService.createReview(request));
        assertEquals(ErrorCode.ALREADY_REVIEWD, ex.getErrorCode());
    }

    @Test
    public void createReview_validData_success() {
        // GIVEN
        UUID productId = UUID.randomUUID();
        CreateReviewRequest request = new CreateReviewRequest();
        request.setProductId(productId);
        request.setRating(5);
        request.setComment("Excellent product!");

        User user = new User();
        Customer customer = new Customer();
        user.setCustomer(customer);

        Product product = new Product();
        Review review = new Review();

        when(userService.getUserFromToken()).thenReturn(user);
        when(productService.getProductEntityById(productId)).thenReturn(product);
        when(reviewMapper.toReview(request)).thenReturn(review);

        // WHEN
        reviewService.createReview(request);

        // THEN
        verify(reviewRepository, times(1)).save(review);
        assertEquals(product, review.getProduct());
        assertEquals(customer, review.getCustomer());

    }

}
