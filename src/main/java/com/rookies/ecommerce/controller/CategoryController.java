package com.rookies.ecommerce.controller;

import com.rookies.ecommerce.dto.request.CategoryRequest;
import com.rookies.ecommerce.dto.response.APIResponse;
import com.rookies.ecommerce.dto.response.MessageResponse;
import com.rookies.ecommerce.service.category.CategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/categories")
public class CategoryController {

    CategoryService categoryService;

    @GetMapping()
    public ResponseEntity<APIResponse> getActiveCategories() {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                categoryService.getActiveCategories()));
    }

    @PostMapping()
    public ResponseEntity<APIResponse> createCategory(@RequestBody @Valid CategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse(MessageResponse.CREATED_SUCCESSFULLY,
                        categoryService.createCategory(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getCategoryById(@PathVariable String id) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                categoryService.getCategoryById(id)));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<APIResponse> getCategoryBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                categoryService.getCategoryBySlug(slug)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse> updateCategory(@PathVariable String id,
                                                      @RequestBody @Valid CategoryRequest request) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.UPDATED_SUCCESSFULLY,
                categoryService.updateCategory(id, request)));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<APIResponse> toggleCategory(@PathVariable String id) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.UPDATED_SUCCESSFULLY,
                categoryService.toggleCategory(id)));
    }

    @GetMapping("/deleted")
    public ResponseEntity<APIResponse> getDeletedCategories() {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                categoryService.getDeletedCategories()));
    }

}
