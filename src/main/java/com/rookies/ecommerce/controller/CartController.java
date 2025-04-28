package com.rookies.ecommerce.controller;

import com.rookies.ecommerce.constant.MessageResponse;
import com.rookies.ecommerce.dto.request.CreateUpdateCartItemRequest;
import com.rookies.ecommerce.dto.request.RemoveCartItemRequest;
import com.rookies.ecommerce.dto.response.APIResponse;
import com.rookies.ecommerce.service.cart.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling cart-related operations.
 */
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/carts")
@Tag(name = "Carts", description = "APIs for managing cart operations")
public class CartController {

    CartService cartService;

    /**
     * Adds an item to the cart.
     *
     * @param request the request object containing details of the item to add
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the quantity of distinct items in cart
     */
    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<APIResponse> addToCart(@RequestBody @Valid CreateUpdateCartItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse(MessageResponse.CREATED_SUCCESSFULLY, cartService.addToCart(request)));
    }

    /**
     * Retrieves the details of the cart with pagination and sorting options.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of items per page (default is 10)
     * @param sortBy the field to sort the items by (default is "createdAt")
     * @param sortDir the direction of sorting (asc for ascending, desc for descending; default is "asc")
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the cart details
     */
    @GetMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<APIResponse> getCartDetail(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "createdAt") String sortBy,
                                                     @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                cartService.getCartDetail(page, size, sortBy, sortDir)));
    }

    /**
     * Updates an item in the cart.
     *
     * @param request the request object containing updated details of the cart item
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the updated quantity of distinct items in cart
     */
    @PatchMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<APIResponse> updateCart(@RequestBody @Valid CreateUpdateCartItemRequest request) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.UPDATED_SUCCESSFULLY,
                cartService.updateCart(request)));
    }

    /**
     * Removes an item from the cart.
     *
     * @param request the request object containing details of the item to remove
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the updated quantity of distinct items in cart
     */
    @DeleteMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<APIResponse> removeItem(@RequestBody RemoveCartItemRequest request) {

        return ResponseEntity.ok(new APIResponse(MessageResponse.UPDATED_SUCCESSFULLY, cartService.removeCartItem(request)));
    }

}