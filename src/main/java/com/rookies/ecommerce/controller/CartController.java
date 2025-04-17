package com.rookies.ecommerce.controller;

import com.rookies.ecommerce.constant.MessageResponse;
import com.rookies.ecommerce.dto.request.CreateUpdateCartItemRequest;
import com.rookies.ecommerce.dto.request.RemoveCartItemRequest;
import com.rookies.ecommerce.dto.response.APIResponse;
import com.rookies.ecommerce.service.cart.CartService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/carts")
public class CartController {

    CartService cartService;

    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<APIResponse> addToCart(@RequestBody @Valid CreateUpdateCartItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse(MessageResponse.CREATED_SUCCESSFULLY, cartService.addToCart(request)));
    }

    @GetMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<APIResponse> getCartDetail(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "createdAt") String sortBy,
                                                     @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                cartService.getCartDetail(page, size, sortBy, sortDir)));
    }

    @PatchMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<APIResponse> updateCart(@RequestBody @Valid CreateUpdateCartItemRequest request) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.UPDATED_SUCCESSFULLY,
                cartService.updateCart(request)));
    }

    @DeleteMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<APIResponse> removeItem(@RequestBody RemoveCartItemRequest request) {
        cartService.removeCartItem(request);
        return ResponseEntity.ok(new APIResponse(MessageResponse.UPDATED_SUCCESSFULLY, null));
    }

}
