package com.rookies.ecommerce.mapper;

import com.rookies.ecommerce.dto.request.CreateFeaturedProduct;
import com.rookies.ecommerce.dto.request.CreateUpdateProductRequest;
import com.rookies.ecommerce.dto.request.UpdateFeaturedProduct;
import com.rookies.ecommerce.dto.response.FeaturedProductResponse;
import com.rookies.ecommerce.dto.response.ProductResponse;
import com.rookies.ecommerce.dto.response.ProductDetailResponse;
import com.rookies.ecommerce.entity.FeaturedProduct;
import com.rookies.ecommerce.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toProduct(CreateUpdateProductRequest info);

    void updateProduct(CreateUpdateProductRequest info, @MappingTarget Product product);

    @Named("toProductDTO")
    ProductResponse toProductDTO(Product product);

    ProductDetailResponse toProductDetail(Product product);

    FeaturedProduct toFeaturedProduct(CreateFeaturedProduct info);

    void updateFeaturedProduct(UpdateFeaturedProduct request, @MappingTarget FeaturedProduct product);

    @Mapping(source = "product", target = "product", qualifiedByName = "toProductDTO")
    FeaturedProductResponse toFeaturedProductResponse(FeaturedProduct product);

}
