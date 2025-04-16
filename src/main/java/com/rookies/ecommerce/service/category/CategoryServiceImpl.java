package com.rookies.ecommerce.service.category;

import com.rookies.ecommerce.dto.request.CreateUpdateCategoryRequest;
import com.rookies.ecommerce.entity.Category;
import com.rookies.ecommerce.entity.User;
import com.rookies.ecommerce.exception.AppException;
import com.rookies.ecommerce.exception.ErrorCode;
import com.rookies.ecommerce.repository.CategoryRepository;
import com.rookies.ecommerce.service.user.UserService;
import com.rookies.ecommerce.utils.SlugUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService{

    CategoryRepository categoryRepository;

    UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Category createCategory(CreateUpdateCategoryRequest request) {
        User user = userService.getUserFromToken();

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .slug(SlugUtil.createSlug(request.getName()))
                .build();

        category.setCreatedBy(user.getId());
        category.setModifiedBy(user.getId());

        return categoryRepository.save(category);
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
    @Transactional(rollbackFor = Exception.class)
    public Category updateCategory(String id, CreateUpdateCategoryRequest request) {
        User user = userService.getUserFromToken();

        Category category = categoryRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setSlug(SlugUtil.createSlug(request.getName()));
        category.setModifiedBy(user.getId());

        return categoryRepository.save(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Category toggleCategory(String id) {
        User user = userService.getUserFromToken();

        Category category = categoryRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        category.setDeleted(!category.isDeleted());
        category.setModifiedBy(user.getId());

        return categoryRepository.save(category);
    }
}
