package com.rookies.ecommerce.service.category;

import com.rookies.ecommerce.dto.request.CategoryRequest;
import com.rookies.ecommerce.entity.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(CategoryRequest request);

    Category getCategoryById(String id);

    Category getCategoryBySlug(String slug);

    List<Category> getActiveCategories();

    List<Category> getDeletedCategories();

    Category updateCategory(String id, CategoryRequest request);

    Category toggleCategory(String id);

}
