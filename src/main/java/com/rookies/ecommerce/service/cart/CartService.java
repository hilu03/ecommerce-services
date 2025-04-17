package com.rookies.ecommerce.service.cart;

import com.rookies.ecommerce.dto.request.CreateUpdateCartItemRequest;
import com.rookies.ecommerce.dto.request.RemoveCartItemRequest;
import com.rookies.ecommerce.dto.response.CartDetailResponse;
import com.rookies.ecommerce.dto.response.CartQuantityResponse;
import org.springframework.data.domain.Page;

public interface CartService {

    CartQuantityResponse addToCart(CreateUpdateCartItemRequest request);

    Page<CartDetailResponse> getCartDetail(int page, int size, String sortBy, String sortDir);

    CartQuantityResponse updateCart(CreateUpdateCartItemRequest request);

    CartQuantityResponse removeCartItem(RemoveCartItemRequest request);

}
