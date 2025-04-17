package com.rookies.ecommerce.mapper;

import com.rookies.ecommerce.dto.response.CartDetailResponse;
import com.rookies.ecommerce.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface CartItemMapper {

    @Mapping(source = "product", target = "product", qualifiedByName = "toProductDTO")
    CartDetailResponse toCartDetailResponse(CartItem cartItem);

}
