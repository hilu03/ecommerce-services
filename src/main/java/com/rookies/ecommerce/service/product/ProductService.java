package com.rookies.ecommerce.service.product;

import com.rookies.ecommerce.dto.request.CreateFeaturedProduct;
import com.rookies.ecommerce.dto.request.CreateUpdateProductRequest;
import com.rookies.ecommerce.dto.request.UpdateFeaturedProduct;
import com.rookies.ecommerce.dto.response.FeaturedProductResponse;
import com.rookies.ecommerce.dto.response.ProductDetailForAdmin;
import com.rookies.ecommerce.dto.response.ProductResponse;
import com.rookies.ecommerce.dto.response.ProductDetailResponse;
import com.rookies.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.util.UUID;

/**
 * Service interface for handling product-related operations.
 */
public interface ProductService {

    /**
     * Creates a new product with the provided details and image file.
     *
     * @param productRequest the request object containing product details
     * @param imageFile the image file associated with the product
     * @throws IOException if an error occurs while processing the image file
     */
    void createProduct(CreateUpdateProductRequest productRequest, MultipartFile imageFile) throws IOException;

    /**
     * Updates an existing product with the provided details and image file.
     *
     * @param id the unique identifier of the product to update
     * @param productRequest the request object containing updated product details
     * @param imageFile the updated image file associated with the product
     * @throws IOException if an error occurs while processing the image file
     */
    void updateProduct(String id, CreateUpdateProductRequest productRequest, MultipartFile imageFile) throws IOException;

    /**
     * Retrieves a paginated list of products based on their deletion status.
     *
     * @param isDeleted the deletion status of the products
     * @param page the page number to retrieve
     * @param size the number of products per page
     * @param sortBy the field to sort the products by
     * @param sortDir the direction of sorting (asc/desc)
     * @return a paginated list of {@link ProductResponse} objects
     */
    Page<ProductResponse> getProductsByIsDeleted(boolean isDeleted, int page, int size, String sortBy, String sortDir);

    /**
     * Retrieves the details of a product by its unique identifier.
     *
     * @param id the unique identifier of the product
     * @return a {@link ProductDetailResponse} containing the product details
     */
    ProductDetailResponse getProductById(String id);

    /**
     * Retrieves detailed information about a product for administrative purposes.
     *
     * @param id the unique identifier of the product
     * @return a {@link ProductDetailForAdmin} containing detailed product information for admin use
     */
    ProductDetailForAdmin getProductDetailForAdmin(String id);

    /**
     * Toggles the active/deleted status of a product.
     *
     * @param id the unique identifier of the product
     * @return true if the status was successfully toggled, false otherwise
     */
    boolean toggleProductStatus(String id);

    /**
     * Retrieves a paginated list of products by category ID and deletion status.
     *
     * @param categoryId the unique identifier of the category
     * @param isDeleted the deletion status of the products
     * @param page the page number to retrieve
     * @param size the number of products per page
     * @param sortBy the field to sort the products by
     * @param sortDir the direction of sorting (asc/desc)
     * @return a paginated list of {@link ProductResponse} objects
     */
    Page<ProductResponse> getProductsByCategoryIdAndIsDeleted(String categoryId, boolean isDeleted, int page,
                                                              int size, String sortBy, String sortDir);

    /**
     * Retrieves the details of a product by its slug.
     *
     * @param slug the slug of the product
     * @return a {@link ProductDetailResponse} containing the product details
     */
    ProductDetailResponse getProductBySlug(String slug);

    /**
     * Adds a new featured product.
     *
     * @param request the request object containing featured product details
     */
    void addFeaturedProduct(CreateFeaturedProduct request);

    /**
     * Updates an existing featured product.
     *
     * @param id the unique identifier of the featured product to update
     * @param request the request object containing updated featured product details
     */
    void updateFeatureProduct(UUID id, UpdateFeaturedProduct request);

    /**
     * Retrieves a paginated list of all featured products.
     *
     * @param page the page number to retrieve
     * @param size the number of featured products per page
     * @param sortBy the field to sort the featured products by
     * @param sortDir the direction of sorting (asc/desc)
     * @return a paginated list of {@link FeaturedProductResponse} objects
     */
    Page<FeaturedProductResponse> getAllFeaturedProduct(int page, int size, String sortBy, String sortDir);

    /**
     * Deletes a featured product by its unique identifier.
     *
     * @param id the unique identifier of the featured product to delete
     */
    void deleteFeaturedProduct(String id);

    /**
     * Retrieves the details of a featured product by its unique identifier.
     *
     * @param id the unique identifier of the featured product
     * @return a {@link FeaturedProductResponse} containing the featured product details
     */
    FeaturedProductResponse getFeaturedProductById(UUID id);

    /**
     * Retrieves a paginated list of all active featured products.
     *
     * @param page the page number to retrieve
     * @param size the number of active featured products per page
     * @param sortBy the field to sort the active featured products by
     * @param sortDir the direction of sorting (asc/desc)
     * @return a paginated list of {@link FeaturedProductResponse} objects
     */
    Page<FeaturedProductResponse> getActiveFeaturedProducts(int page, int size, String sortBy, String sortDir);

    /**
     * Retrieves the product entity by its unique identifier.
     *
     * @param id the unique identifier of the product
     * @return the {@link Product} entity
     */
    Product getProductEntityById(UUID id);

    /**
     * Searches for products by their name.
     *
     * @param name the name of the product to search for
     * @param page the page number to retrieve
     * @param size the number of products per page
     * @param sortBy the field to sort the products by
     * @param sortDir the direction of sorting (asc/desc)
     * @return a paginated list of {@link ProductResponse} objects matching the search criteria
     */
    Page<ProductResponse> searchByName(String name, int page, int size, String sortBy, String sortDir);
}