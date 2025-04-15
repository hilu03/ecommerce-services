package com.rookies.ecommerce.service.product;

import com.rookies.ecommerce.dto.request.CreateFeaturedProduct;
import com.rookies.ecommerce.dto.request.CreateUpdateProductRequest;
import com.rookies.ecommerce.dto.request.UpdateFeaturedProduct;
import com.rookies.ecommerce.dto.response.FeaturedProductResponse;
import com.rookies.ecommerce.dto.response.ProductResponse;
import com.rookies.ecommerce.dto.response.ProductDetailResponse;
import com.rookies.ecommerce.entity.Category;
import com.rookies.ecommerce.entity.FeaturedProduct;
import com.rookies.ecommerce.entity.Product;
import com.rookies.ecommerce.exception.AppException;
import com.rookies.ecommerce.exception.ErrorCode;
import com.rookies.ecommerce.mapper.ProductMapper;
import com.rookies.ecommerce.repository.FeaturedProductRepository;
import com.rookies.ecommerce.repository.ProductRepository;
import com.rookies.ecommerce.service.category.CategoryService;
import com.rookies.ecommerce.service.upload.UploadService;
import com.rookies.ecommerce.utils.SlugUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;

    CategoryService categoryService;

    ProductMapper productMapper;

    UploadService uploadService;

    FeaturedProductRepository featuredProductRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createProduct(CreateUpdateProductRequest productRequest, MultipartFile imageFile) throws IOException {
        Category category = categoryService.getCategoryById(productRequest.getCategoryId());
        Product product = productMapper.toProduct(productRequest);
        product.setCategory(category);
        productRepository.save(product);

        String imageUrl = uploadService.uploadFile(imageFile, "products", product.getId().toString());
        product.setImageUrl(imageUrl);
        product.setSlug(SlugUtil.createSlug(productRequest.getName()) + "-" + product.getId());
        productRepository.save(product);
    }

    @Override
    public void updateProduct(String id, CreateUpdateProductRequest productRequest, MultipartFile imageFile) throws IOException {
        Product product = productRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        Category category = categoryService.getCategoryById(productRequest.getCategoryId());

        product.setCategory(category);
        productMapper.updateProduct(productRequest, product);
        product.setSlug(SlugUtil.createSlug(productRequest.getName()) + "-" + product.getId());

        if (imageFile != null) {
            String imageUrl = uploadService.uploadFile(imageFile, "products", product.getId().toString());
            product.setImageUrl(imageUrl);
        }

        productRepository.save(product);
    }

    @Override
    public Page<ProductResponse> getProductsByIsDeleted(boolean isDeleted, int page, int size, String sortBy, String sortDir) {
        return productRepository.findAllByIsDeleted(isDeleted, PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sortDir), sortBy))).map(productMapper::toProductDTO);
    }

    @Override
    public ProductDetailResponse getProductById(String id) {
        Product product = productRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        return productMapper.toProductDetail(product);
    }

    @Override
    public boolean toggleProductStatus(String id) {
        Product product = productRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        product.setDeleted(!product.isDeleted());
        productRepository.save(product);
        return product.isDeleted();
    }

    @Override
    public Page<ProductResponse> getProductsByCategoryIdAndIsDeleted(String categoryId, boolean isDeleted, int page, int size,
                                                                     String sortBy, String sortDir) {
        Category category = categoryService.getCategoryById(categoryId);

        return productRepository.findAllByCategoryAndIsDeleted(category, false,
                PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy)))
                .map(productMapper::toProductDTO);
    }

    @Override
    public ProductDetailResponse getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        return productMapper.toProductDetail(product);
    }

    @Override
    public void addFeaturedProduct(CreateFeaturedProduct request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        FeaturedProduct featuredProduct = productMapper.toFeaturedProduct(request);
        featuredProduct.setProduct(product);
        featuredProductRepository.save(featuredProduct);
    }

    @Override
    public void updateFeatureProduct(UUID id, UpdateFeaturedProduct request) {
        FeaturedProduct featuredProduct = featuredProductRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        productMapper.updateFeaturedProduct(request, featuredProduct);
        featuredProductRepository.save(featuredProduct);
    }

    @Override
    public Page<FeaturedProductResponse> getAllFeaturedProduct(int page, int size, String sortBy, String sortDir) {
        return featuredProductRepository
                .findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy)))
                .map(productMapper::toFeaturedProductResponse);
    }

    @Override
    public void deleteFeaturedProduct(String id) {
        FeaturedProduct featuredProduct = featuredProductRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        featuredProductRepository.delete(featuredProduct);
    }

    @Override
    public FeaturedProductResponse getFeaturedProductById(UUID id) {
        FeaturedProduct featuredProduct = featuredProductRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        return productMapper.toFeaturedProductResponse(featuredProduct);
    }

    @Override
    public Page<FeaturedProductResponse> getActiveFeaturedProducts(int page, int size, String sortBy, String sortDir) {
        Date now = new Date();
        return featuredProductRepository.findByStartDateBeforeAndEndDateAfter(now, now,
                PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy)))
                .map(productMapper::toFeaturedProductResponse);
    }

    @Override
    public Product getProductEntityById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
    }


}
