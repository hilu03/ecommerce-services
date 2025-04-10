package com.rookies.ecommerce.service.category;

import com.rookies.ecommerce.dto.request.CategoryRequest;
import com.rookies.ecommerce.entity.Category;
import com.rookies.ecommerce.exception.AppException;
import com.rookies.ecommerce.exception.ErrorCode;
import com.rookies.ecommerce.repository.CategoryRepository;
import com.rookies.ecommerce.utils.SlugUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService{

    CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CategoryRequest request) {
        return categoryRepository.save(
                Category.builder()
                        .name(request.getName())
                        .description(request.getDescription())
                        .slug(SlugUtil.createSlug(request.getName()))
                        .build()
        );
    }

    @Override
    public Category getCategoryById(String id) {
        return categoryRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    @Override
    public Category getCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    @Override
    public List<Category> getActiveCategories() {
        return categoryRepository.findAllByIsDeletedOrderByNameAsc(false);
    }

    @Override
    public List<Category> getDeletedCategories() {
        return categoryRepository.findAllByIsDeletedOrderByNameAsc(true);
    }

    @Override
    public Category updateCategory(String id, CategoryRequest request) {
        Category category = categoryRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setSlug(SlugUtil.createSlug(request.getName()));
        return categoryRepository.save(category);
    }

    @Override
    public Category toggleCategory(String id) {
        Category category = categoryRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        category.setDeleted(!category.isDeleted());
        return categoryRepository.save(category);
    }
}
