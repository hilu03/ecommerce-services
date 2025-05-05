package com.rookies.ecommerce.service.product;

import com.rookies.ecommerce.dto.request.CreateFeaturedProduct;
import com.rookies.ecommerce.dto.request.CreateUpdateProductRequest;
import com.rookies.ecommerce.dto.request.UpdateFeaturedProduct;
import com.rookies.ecommerce.dto.response.FeaturedProductResponse;
import com.rookies.ecommerce.dto.response.ProductDetailForAdmin;
import com.rookies.ecommerce.dto.response.ProductResponse;
import com.rookies.ecommerce.dto.response.ProductDetailResponse;
import com.rookies.ecommerce.entity.*;
import com.rookies.ecommerce.exception.AppException;
import com.rookies.ecommerce.exception.ErrorCode;
import com.rookies.ecommerce.mapper.ProductMapper;
import com.rookies.ecommerce.repository.FeaturedProductRepository;
import com.rookies.ecommerce.repository.ProductRepository;
import com.rookies.ecommerce.service.category.CategoryService;
import com.rookies.ecommerce.service.upload.UploadService;
import com.rookies.ecommerce.service.user.UserService;
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
import java.util.List;
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

    UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createProduct(CreateUpdateProductRequest productRequest, MultipartFile imageFile) throws IOException {
        User user = userService.getUserFromToken();

        Category category = categoryService.getCategoryById(productRequest.getCategoryId());
        Product product = productMapper.toProduct(productRequest);
        product.setCategory(category);
        productRepository.save(product);

        String imageUrl = uploadService.uploadFile(imageFile, "products", product.getId().toString());
        product.setImageUrl(imageUrl);
        product.setSlug(SlugUtil.createSlug(productRequest.getName()) + "-" + product.getId());
        product.setCreatedBy(user.getId());
        product.setModifiedBy(user.getId());

        productRepository.save(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProduct(String id, CreateUpdateProductRequest productRequest, MultipartFile imageFile) throws IOException {
        User user = userService.getUserFromToken();

        Product product = productRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        Category category = categoryService.getCategoryById(productRequest.getCategoryId());

        product.setCategory(category);
        productMapper.updateProduct(productRequest, product);
        product.setSlug(SlugUtil.createSlug(productRequest.getName()) + "-" + product.getId());
        product.setModifiedBy(user.getId());

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
    public ProductDetailForAdmin getProductDetailForAdmin(String id) {
        Product product = productRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        ProductDetailForAdmin productDetailForAdmin = productMapper.toProductDetailForAdmin(product);
        UserProfile createdByUser = userService.getUserEntityById(product.getCreatedBy()).getUserProfile();
        UserProfile modifiedByByUser = userService.getUserEntityById(product.getModifiedBy()).getUserProfile();
        productDetailForAdmin.setCreatedBy(createdByUser.getFullName());
        productDetailForAdmin.setModifiedBy(modifiedByByUser.getFullName());
        return productDetailForAdmin;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean toggleProductStatus(String id) {
        User user = userService.getUserFromToken();
        Product product = productRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        product.setDeleted(!product.isDeleted());
        product.setModifiedBy(user.getId());
        productRepository.save(product);
        return product.isDeleted();
    }

    @Override
    public Page<ProductResponse> getProductsByCategoryIdAndIsDeleted(String categoryId, boolean isDeleted, int page, int size,
                                                                     String sortBy, String sortDir) {
        Category category = categoryService.getCategoryById(categoryId);

        return productRepository.findAllByCategoryAndIsDeleted(category, isDeleted,
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
    @Transactional(rollbackFor = Exception.class)
    public void addFeaturedProduct(CreateFeaturedProduct request) {
        User user = userService.getUserFromToken();

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        List<FeaturedProduct> overlaps = featuredProductRepository
                .findOverlappingForCreate(request.getProductId(), request.getStartDate(), request.getEndDate());

        if (!overlaps.isEmpty()) {
            throw new AppException(ErrorCode.OVERLAPPING_FEATURED_PRODUCT);
        }

        FeaturedProduct featuredProduct = productMapper.toFeaturedProduct(request);
        featuredProduct.setProduct(product);
        featuredProduct.setCreatedBy(user.getId());
        featuredProduct.setModifiedBy(user.getId());

        featuredProductRepository.save(featuredProduct);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFeatureProduct(UUID id, UpdateFeaturedProduct request) {
        User user = userService.getUserFromToken();

        FeaturedProduct featuredProduct = featuredProductRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        List<FeaturedProduct> overlaps = featuredProductRepository
                .findOverlappingForUpdate(
                        featuredProduct.getProduct().getId(),
                        request.getStartDate(),
                        request.getEndDate(),
                        featuredProduct.getId()
                );

        if (!overlaps.isEmpty()) {
            throw new AppException(ErrorCode.OVERLAPPING_FEATURED_PRODUCT);
        }

        productMapper.updateFeaturedProduct(request, featuredProduct);
        featuredProduct.setModifiedBy(user.getId());

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
        return featuredProductRepository.findActiveFeaturedProducts(now,
                PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy)))
                .map(productMapper::toFeaturedProductResponse);
    }

    @Override
    public Product getProductEntityById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    @Override
    public Page<ProductResponse> searchByName(String name, int page, int size, String sortBy, String sortDir) {
        return productRepository.findByNameContainingAndIsDeleted(name, false,
                        PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy)))
                .map(productMapper::toProductDTO);
    }


}
