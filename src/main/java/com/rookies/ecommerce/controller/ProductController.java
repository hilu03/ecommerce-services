package com.rookies.ecommerce.controller;

import com.rookies.ecommerce.dto.request.CreateFeaturedProduct;
import com.rookies.ecommerce.dto.request.CreateUpdateProductRequest;
import com.rookies.ecommerce.dto.request.UpdateFeaturedProduct;
import com.rookies.ecommerce.dto.response.APIResponse;
import com.rookies.ecommerce.constant.MessageResponse;
import com.rookies.ecommerce.exception.AppException;
import com.rookies.ecommerce.exception.ErrorCode;
import com.rookies.ecommerce.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/products")
public class ProductController {

    ProductService productService;

    @GetMapping()
    public ResponseEntity<APIResponse> getActiveProducts(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam(defaultValue = "name") String sortBy,
                                                         @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                productService.getProductsByIsDeleted(false, page, size, sortBy, sortDir)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/hidden")
    public ResponseEntity<APIResponse> getHiddenProducts(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam(defaultValue = "name") String sortBy,
                                                         @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                productService.getProductsByIsDeleted(true, page, size, sortBy, sortDir)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getProductById(@PathVariable String id) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                productService.getProductById(id)));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<APIResponse> getProductBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                productService.getProductBySlug(slug)));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<APIResponse> getProductsByCategoryId(@PathVariable String categoryId,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @RequestParam(defaultValue = "name") String sortBy,
                                                                @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                productService.getProductsByCategoryIdAndIsDeleted(categoryId, false, page, size, sortBy, sortDir)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
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

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
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

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<APIResponse> toggleProductStatus(@PathVariable String id) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.UPDATED_SUCCESSFULLY,
                productService.toggleProductStatus(id)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/feature")
    public ResponseEntity<APIResponse> addFeaturedProduct(@RequestBody @Valid CreateFeaturedProduct request) {
        productService.addFeaturedProduct(request);
        return ResponseEntity.ok(new APIResponse(MessageResponse.CREATED_SUCCESSFULLY, null));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/feature/{id}")
    public ResponseEntity<APIResponse> updateFeaturedProduct(@PathVariable String id,
                                                             @RequestBody @Valid UpdateFeaturedProduct request) {
        productService.updateFeatureProduct(UUID.fromString(id), request);
        return ResponseEntity.ok(new APIResponse(MessageResponse.CREATED_SUCCESSFULLY, null));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/feature/{id}")
    public ResponseEntity<APIResponse> deleteFeaturedProduct(@PathVariable String id) {
        productService.deleteFeaturedProduct(id);
        return ResponseEntity.ok(new APIResponse(MessageResponse.SUCCESS_REQUEST, null));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/feature")
    public ResponseEntity<APIResponse> getAllFeaturedProduct(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(defaultValue = "createdAt") String sortBy,
                                                             @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                productService.getAllFeaturedProduct(page, size, sortBy, sortDir)));
    }

    @GetMapping("/feature/{id}")
    public ResponseEntity<APIResponse> getFeaturedProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                productService.getFeaturedProductById(id)));
    }

    @GetMapping("/feature/active")
    public ResponseEntity<APIResponse> getActiveFeaturedProduct(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(defaultValue = "createdAt") String sortBy,
                                                             @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                productService.getActiveFeaturedProducts(page, size, sortBy, sortDir)));
    }

}
