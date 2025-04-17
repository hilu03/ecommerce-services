package com.rookies.ecommerce.service.cart;

import com.rookies.ecommerce.dto.request.CreateUpdateCartItemRequest;
import com.rookies.ecommerce.dto.request.RemoveCartItemRequest;
import com.rookies.ecommerce.dto.response.CartDetailResponse;
import com.rookies.ecommerce.dto.response.CartQuantityResponse;
import com.rookies.ecommerce.entity.Cart;
import com.rookies.ecommerce.entity.CartItem;
import com.rookies.ecommerce.entity.Product;
import com.rookies.ecommerce.entity.User;
import com.rookies.ecommerce.exception.AppException;
import com.rookies.ecommerce.exception.ErrorCode;
import com.rookies.ecommerce.mapper.CartItemMapper;
import com.rookies.ecommerce.repository.CartItemRepository;
import com.rookies.ecommerce.service.product.ProductService;
import com.rookies.ecommerce.service.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService{

    CartItemRepository cartItemRepository;

    UserService userService;

    ProductService productService;

    CartItemMapper cartItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CartQuantityResponse addToCart(CreateUpdateCartItemRequest request) {
        User user = userService.getUserFromToken();
        Product product = productService.getProductEntityById(request.getProductId());

        if (product.getAvailableQuantity() < request.getQuantity()) {
            throw new AppException(ErrorCode.QUANTITY_EXCEED);
        }

        Cart cart = user.getCustomer().getCart();

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product);

        if (cartItem == null) {
            cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();
        }
        else if (product.getAvailableQuantity() < cartItem.getQuantity() + request.getQuantity()) {
            throw new AppException(ErrorCode.QUANTITY_EXCEED);
        }
        else {
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        }

        cartItemRepository.save(cartItem);

        return CartQuantityResponse.builder()
                .count(cartItemRepository.countByCart(cart))
                .build();

    }

    @Override
    public Page<CartDetailResponse> getCartDetail(int page, int size, String sortBy, String sortDir) {
        User user = userService.getUserFromToken();
        Cart cart = user.getCustomer().getCart();

        return cartItemRepository.findByCart(cart, PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sortDir), sortBy))).map(cartItemMapper::toCartDetailResponse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CartQuantityResponse updateCart(CreateUpdateCartItemRequest request) {
        User user = userService.getUserFromToken();
        Product product = productService.getProductEntityById(request.getProductId());
        Cart cart = user.getCustomer().getCart();
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product);

        if (cartItem == null) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND);
        }

        if (product.getAvailableQuantity() < request.getQuantity()) {
            throw new AppException(ErrorCode.QUANTITY_EXCEED);
        }

        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);

        return CartQuantityResponse.builder()
                .count(cartItemRepository.countByCart(cart))
                .build();
    }

    @Override
    public CartQuantityResponse removeCartItem(RemoveCartItemRequest request) {
        User user = userService.getUserFromToken();
        Cart cart = user.getCustomer().getCart();

        for (UUID id: request.getProductIds()) {
            Product product = productService.getProductEntityById(id);
            CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product);

            if (cartItem == null) {
                throw new AppException(ErrorCode.RESOURCE_NOT_FOUND);
            }

            cartItemRepository.delete(cartItem);
        }

        return CartQuantityResponse.builder()
                .count(cartItemRepository.countByCart(cart))
                .build();

    }
}
