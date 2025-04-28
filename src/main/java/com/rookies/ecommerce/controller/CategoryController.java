package com.rookies.ecommerce.controller;

import com.rookies.ecommerce.dto.request.CreateUpdateCategoryRequest;
import com.rookies.ecommerce.dto.response.APIResponse;
import com.rookies.ecommerce.constant.MessageResponse;
import com.rookies.ecommerce.service.category.CategoryService;
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
 * Controller for managing and retrieving category information.
 */
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/categories")
@Tag(name = "Categories", description = "APIs for managing and retrieving category information")
public class CategoryController {

    CategoryService categoryService;

    /**
     * Retrieves a list of all active categories.
     *
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the list of active categories
     */
    @GetMapping()
    public ResponseEntity<APIResponse> getActiveCategories() {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                categoryService.getActiveCategories()));
    }

    /**
     * Creates a new category.
     *
     * @param request the request object containing category details
     * @return a {@link ResponseEntity} containing an {@link APIResponse} indicating the creation status
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<APIResponse> createCategory(@RequestBody @Valid CreateUpdateCategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse(MessageResponse.CREATED_SUCCESSFULLY,
                        categoryService.createCategory(request)));
    }

    /**
     * Retrieves a category by its unique identifier.
     *
     * @param id the unique identifier of the category
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the category details
     */
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getCategoryById(@PathVariable String id) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                categoryService.getCategoryById(id)));
    }

    /**
     * Retrieves a category by its slug.
     *
     * @param slug the slug of the category
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the category details
     */
    @GetMapping("/slug/{slug}")
    public ResponseEntity<APIResponse> getCategoryBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                categoryService.getCategoryBySlug(slug)));
    }

    /**
     * Updates an existing category by its unique identifier.
     *
     * @param id the unique identifier of the category
     * @param request the request object containing updated category details
     * @return a {@link ResponseEntity} containing an {@link APIResponse} indicating the update status
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse> updateCategory(@PathVariable String id,
                                                      @RequestBody @Valid CreateUpdateCategoryRequest request) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.UPDATED_SUCCESSFULLY,
                categoryService.updateCategory(id, request)));
    }

    /**
     * Toggles the active status of a category by its unique identifier.
     *
     * @param id the unique identifier of the category
     * @return a {@link ResponseEntity} containing an {@link APIResponse} indicating the toggle status
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<APIResponse> toggleCategory(@PathVariable String id) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.UPDATED_SUCCESSFULLY,
                categoryService.toggleCategory(id)));
    }

    /**
     * Retrieves a list of all deleted categories.
     *
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the list of deleted categories
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/deleted")
    public ResponseEntity<APIResponse> getDeletedCategories() {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                categoryService.getDeletedCategories()));
    }

}