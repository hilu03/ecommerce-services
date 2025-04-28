package com.rookies.ecommerce.service.category;

import com.rookies.ecommerce.dto.request.CreateUpdateCategoryRequest;
import com.rookies.ecommerce.entity.Category;

import java.util.List;

/**
 * Service interface for handling category-related operations.
 */
public interface CategoryService {

    /**
     * Creates a new category.
     *
     * @param request the request object containing category creation details
     * @return the created {@link Category} entity
     */
    Category createCategory(CreateUpdateCategoryRequest request);

    /**
     * Retrieves a category by its unique identifier.
     *
     * @param id the unique identifier of the category
     * @return the {@link Category} entity with the specified ID
     */
    Category getCategoryById(String id);

    /**
     * Retrieves a category by its slug.
     *
     * @param slug the slug of the category
     * @return the {@link Category} entity with the specified slug
     */
    Category getCategoryBySlug(String slug);

    /**
     * Retrieves a list of all active categories.
     *
     * @return a list of active {@link Category} entities
     */
    List<Category> getActiveCategories();

    /**
     * Retrieves a list of all deleted categories.
     *
     * @return a list of deleted {@link Category} entities
     */
    List<Category> getDeletedCategories();

    /**
     * Updates an existing category.
     *
     * @param id the unique identifier of the category to update
     * @param request the request object containing updated category details
     * @return the updated {@link Category} entity
     */
    Category updateCategory(String id, CreateUpdateCategoryRequest request);

    /**
     * Toggles the active/deleted status of a category.
     *
     * @param id the unique identifier of the category to toggle
     * @return the updated {@link Category} entity with the toggled status
     */
    Category toggleCategory(String id);

}