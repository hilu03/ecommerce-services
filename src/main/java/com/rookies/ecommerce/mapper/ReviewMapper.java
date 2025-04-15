package com.rookies.ecommerce.mapper;

import com.rookies.ecommerce.dto.request.CreateReviewRequest;
import com.rookies.ecommerce.dto.request.UpdateReviewRequest;
import com.rookies.ecommerce.dto.response.AllReviewResponse;
import com.rookies.ecommerce.dto.response.ReviewDetailResponse;
import com.rookies.ecommerce.dto.response.ReviewResponse;
import com.rookies.ecommerce.dto.response.UserReviewResponse;
import com.rookies.ecommerce.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { UserMapper.class, ProductMapper.class })
public interface ReviewMapper {

    Review toReview(CreateReviewRequest request);

    void updateReview(UpdateReviewRequest request, @MappingTarget Review review);

    @Mapping(source = "customer", target = "customer", qualifiedByName = "toCustomerDTO")
    ReviewResponse toReviewResponse(Review review);

    @Mapping(source = "customer", target = "customer", qualifiedByName = "toCustomerDTO")
    @Mapping(source = "product", target = "product", qualifiedByName = "toProductDTO")
    ReviewDetailResponse toReviewDetail(Review review);

    @Mapping(source = "product", target = "product", qualifiedByName = "toProductDTO")
    UserReviewResponse toUserReviewResponse(Review review);

    @Mapping(source = "product", target = "product", qualifiedByName = "toProductDTO")
    @Mapping(source = "customer", target = "customer", qualifiedByName = "toCustomerWithEmailDTO")
    AllReviewResponse toAllReviewResponse(Review review);

}
