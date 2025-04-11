package com.rookies.ecommerce.service.product;

import com.rookies.ecommerce.dto.request.CreateUpdateProductRequest;
import com.rookies.ecommerce.dto.response.ProductResponse;
import com.rookies.ecommerce.dto.response.ProductDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {

    void createProduct(CreateUpdateProductRequest productRequest, MultipartFile imageFile) throws IOException;

    void updateProduct(String id, CreateUpdateProductRequest productRequest, MultipartFile imageFile) throws IOException;

    Page<ProductResponse> getProductsByIsDeleted(boolean isDeleted, int page, int size, String sortBy, String sortDir);

    ProductDetailResponse getProductById(String id);

    boolean toggleProductStatus(String id);

    Page<ProductResponse> getProductsByCategoryIdAndIsDeleted(String categoryId, boolean isDeleted, int page,
                                                              int size, String sortBy, String sortDir);

    ProductDetailResponse getProductBySlug(String slug);
}
