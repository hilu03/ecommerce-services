package com.rookies.ecommerce.mapper;

import com.rookies.ecommerce.dto.request.CreateUpdateProductRequest;
import com.rookies.ecommerce.dto.response.ProductResponse;
import com.rookies.ecommerce.dto.response.ProductDetailResponse;
import com.rookies.ecommerce.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toProduct(CreateUpdateProductRequest info);

    void updateProduct(CreateUpdateProductRequest info, @MappingTarget Product product);

    ProductResponse toProductDTO(Product product);

    ProductDetailResponse toProductDetail(Product product);

}
