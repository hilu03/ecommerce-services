package com.rookies.ecommerce.controller;

import com.rookies.ecommerce.dto.request.CreateFeaturedProduct;
import com.rookies.ecommerce.dto.request.CreateUpdateProductRequest;
import com.rookies.ecommerce.dto.request.UpdateFeaturedProduct;
import com.rookies.ecommerce.dto.response.APIResponse;
import com.rookies.ecommerce.constant.MessageResponse;
import com.rookies.ecommerce.exception.AppException;
import com.rookies.ecommerce.exception.ErrorCode;
import com.rookies.ecommerce.service.product.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

/**
 * Controller for managing and retrieving product information.
 */
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/products")
@Tag(name = "Products", description = "APIs for managing and retrieving product information")
public class ProductController {

    ProductService productService;

    /**
     * Retrieves a paginated list of active products.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of products per page (default is 10)
     * @param sortBy the field to sort the products by (default is "name")
     * @param sortDir the direction of sorting (asc for ascending, desc for descending; default is "asc")
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the list of active products
     */
    @GetMapping()
    public ResponseEntity<APIResponse> getActiveProducts(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam(defaultValue = "name") String sortBy,
                                                         @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                productService.getProductsByIsDeleted(false, page, size, sortBy, sortDir)));
    }

    /**
     * Retrieves a paginated list of hidden products.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of products per page (default is 10)
     * @param sortBy the field to sort the products by (default is "name")
     * @param sortDir the direction of sorting (asc for ascending, desc for descending; default is "asc")
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the list of hidden products
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/hidden")
    public ResponseEntity<APIResponse> getHiddenProducts(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam(defaultValue = "name") String sortBy,
                                                         @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                productService.getProductsByIsDeleted(true, page, size, sortBy, sortDir)));
    }

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id the unique identifier of the product
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the product details
     */
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getProductById(@PathVariable String id) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                productService.getProductById(id)));
    }

    /**
     * Retrieves a product by its slug.
     *
     * @param slug the slug of the product
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the product details
     */
    @GetMapping("/slug/{slug}")
    public ResponseEntity<APIResponse> getProductBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                productService.getProductBySlug(slug)));
    }

    /**
     * Retrieves a paginated list of products by category ID.
     *
     * @param categoryId the unique identifier of the category
     * @param page the page number to retrieve (default is 0)
     * @param size the number of products per page (default is 10)
     * @param sortBy the field to sort the products by (default is "name")
     * @param sortDir the direction of sorting (asc for ascending, desc for descending; default is "asc")
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the list of products
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<APIResponse> getProductsByCategoryId(@PathVariable String categoryId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size,
                                                               @RequestParam(defaultValue = "name") String sortBy,
                                                               @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                productService.getProductsByCategoryIdAndIsDeleted(categoryId, false, page, size, sortBy, sortDir)));
    }

    /**
     * Creates a new product with an image file and its related information.
     *
     * @param productInfo the request object containing product details
     * @param imageFile the image file to associate with the product
     * @return a {@link ResponseEntity} containing an {@link APIResponse} indicating the creation status
     * @throws IOException if an error occurs while processing the image file
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<APIResponse> createProduct(@RequestPart @Valid CreateUpdateProductRequest productInfo,
                                                     @RequestPart MultipartFile imageFile) throws IOException {

        if (imageFile == null) {
            throw new AppException(ErrorCode.INVALID_IMAGE_FILE);
        }

        if (imageFile.isEmpty() || imageFile.getSize() == 0) {
            throw new AppException(ErrorCode.INVALID_IMAGE_FILE);
        }

        if (!Objects.requireNonNull(imageFile.getContentType()).contains("image/")) {
            throw new AppException(ErrorCode.INVALID_IMAGE_FILE);
        }

        if (imageFile.getSize() > 5 * 1024 * 1024) {
            throw new AppException(ErrorCode.INVALID_IMAGE_FILE);
        }

        productService.createProduct(productInfo, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse(MessageResponse.CREATED_SUCCESSFULLY, null));
    }

    /**
     * Updates an existing product by its ID, with an optional image file.
     *
     * @param id the unique identifier of the product
     * @param productInfo the request object containing updated product details
     * @param imageFile the optional image file to update the product with
     * @return a {@link ResponseEntity} containing an {@link APIResponse} indicating the update status
     * @throws IOException if an error occurs while processing the image file
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<APIResponse> updateProduct(@PathVariable String id,
                                                     @RequestPart @Valid CreateUpdateProductRequest productInfo,
                                                     @RequestPart(required = false) MultipartFile imageFile) throws IOException {

        if (imageFile != null && (imageFile.isEmpty() || imageFile.getSize() == 0)) {
            throw new AppException(ErrorCode.INVALID_IMAGE_FILE);
        }
        if (imageFile != null && !Objects.requireNonNull(imageFile.getContentType()).contains("image/")) {
            throw new AppException(ErrorCode.INVALID_IMAGE_FILE);
        }
        if (imageFile != null && imageFile.getSize() > 5 * 1024 * 1024) {
            throw new AppException(ErrorCode.INVALID_IMAGE_FILE);
        }

        productService.updateProduct(id, productInfo, imageFile);
        return ResponseEntity.ok(new APIResponse(MessageResponse.UPDATED_SUCCESSFULLY, null));
    }

    /**
     * Toggles the active status of a product by its ID.
     *
     * @param id the unique identifier of the product
     * @return a {@link ResponseEntity} containing an {@link APIResponse} indicating the toggle status
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<APIResponse> toggleProductStatus(@PathVariable String id) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.UPDATED_SUCCESSFULLY,
                productService.toggleProductStatus(id)));
    }

    /**
     * Adds a product to the featured products list.
     *
     * @param request the request object containing details of the featured product
     * @return a {@link ResponseEntity} containing an {@link APIResponse} indicating the addition status
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/feature")
    public ResponseEntity<APIResponse> addFeaturedProduct(@RequestBody @Valid CreateFeaturedProduct request) {
        productService.addFeaturedProduct(request);
        return ResponseEntity.ok(new APIResponse(MessageResponse.CREATED_SUCCESSFULLY, null));
    }

    /**
     * Updates a featured product by its ID.
     *
     * @param id the unique identifier of the featured product
     * @param request the request object containing updated details of the featured product
     * @return a {@link ResponseEntity} containing an {@link APIResponse} indicating the update status
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/feature/{id}")
    public ResponseEntity<APIResponse> updateFeaturedProduct(@PathVariable String id,
                                                             @RequestBody @Valid UpdateFeaturedProduct request) {
        productService.updateFeatureProduct(UUID.fromString(id), request);
        return ResponseEntity.ok(new APIResponse(MessageResponse.CREATED_SUCCESSFULLY, null));
    }

    /**
     * Deletes a featured product by its ID.
     *
     * @param id the unique identifier of the featured product
     * @return a {@link ResponseEntity} containing an {@link APIResponse} indicating the deletion status
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/feature/{id}")
    public ResponseEntity<APIResponse> deleteFeaturedProduct(@PathVariable String id) {
        productService.deleteFeaturedProduct(id);
        return ResponseEntity.ok(new APIResponse(MessageResponse.SUCCESS_REQUEST, null));
    }

    /**
     * Retrieves a paginated list of all featured products.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of featured products per page (default is 10)
     * @param sortBy the field to sort the featured products by (default is "createdAt")
     * @param sortDir the direction of sorting (asc for ascending, desc for descending; default is "asc")
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the list of featured products
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/feature")
    public ResponseEntity<APIResponse> getAllFeaturedProduct(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(defaultValue = "createdAt") String sortBy,
                                                             @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                productService.getAllFeaturedProduct(page, size, sortBy, sortDir)));
    }

    /**
     * Retrieves a featured product by its unique identifier.
     *
     * @param id the unique identifier of the featured product
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the featured product details
     */
    @GetMapping("/feature/{id}")
    public ResponseEntity<APIResponse> getFeaturedProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                productService.getFeaturedProductById(id)));
    }

    /**
     * Retrieves a paginated list of active featured products.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of active featured products per page (default is 10)
     * @param sortBy the field to sort the active featured products by (default is "createdAt")
     * @param sortDir the direction of sorting (asc for ascending, desc for descending; default is "asc")
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the list of active featured products
     */
    @GetMapping("/feature/active")
    public ResponseEntity<APIResponse> getActiveFeaturedProduct(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                productService.getActiveFeaturedProducts(page, size, sortBy, sortDir)));
    }

}