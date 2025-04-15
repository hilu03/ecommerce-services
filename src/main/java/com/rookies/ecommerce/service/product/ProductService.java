package com.rookies.ecommerce.service.product;

import com.rookies.ecommerce.dto.request.CreateFeaturedProduct;
import com.rookies.ecommerce.dto.request.CreateUpdateProductRequest;
import com.rookies.ecommerce.dto.request.UpdateFeaturedProduct;
import com.rookies.ecommerce.dto.response.FeaturedProductResponse;
import com.rookies.ecommerce.dto.response.ProductResponse;
import com.rookies.ecommerce.dto.response.ProductDetailResponse;
import com.rookies.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.util.UUID;

public interface ProductService {

    void createProduct(CreateUpdateProductRequest productRequest, MultipartFile imageFile) throws IOException;

    void updateProduct(String id, CreateUpdateProductRequest productRequest, MultipartFile imageFile) throws IOException;

    Page<ProductResponse> getProductsByIsDeleted(boolean isDeleted, int page, int size, String sortBy, String sortDir);

    ProductDetailResponse getProductById(String id);

    boolean toggleProductStatus(String id);

    Page<ProductResponse> getProductsByCategoryIdAndIsDeleted(String categoryId, boolean isDeleted, int page,
                                                              int size, String sortBy, String sortDir);

    ProductDetailResponse getProductBySlug(String slug);

    void addFeaturedProduct(CreateFeaturedProduct request);

    void updateFeatureProduct(UUID id, UpdateFeaturedProduct request);

    Page<FeaturedProductResponse> getAllFeaturedProduct(int page, int size, String sortBy, String sortDir);

    void deleteFeaturedProduct(String id);

    FeaturedProductResponse getFeaturedProductById(UUID id);

    Page<FeaturedProductResponse> getActiveFeaturedProducts(int page, int size, String sortBy, String sortDir);

    Product getProductEntityById(UUID id);
}
