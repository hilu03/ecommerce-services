package com.rookies.ecommerce.service;

import com.rookies.ecommerce.dto.request.CreateUpdateProductRequest;
import com.rookies.ecommerce.dto.response.ProductDetailResponse;
import com.rookies.ecommerce.dto.response.ProductResponse;
import com.rookies.ecommerce.entity.Category;
import com.rookies.ecommerce.entity.Product;
import com.rookies.ecommerce.entity.User;
import com.rookies.ecommerce.exception.AppException;
import com.rookies.ecommerce.exception.ErrorCode;
import com.rookies.ecommerce.mapper.ProductMapper;
import com.rookies.ecommerce.repository.ProductRepository;
import com.rookies.ecommerce.service.category.CategoryService;
import com.rookies.ecommerce.service.product.ProductServiceImpl;
import com.rookies.ecommerce.service.upload.UploadService;
import com.rookies.ecommerce.service.user.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductServiceUnitTests {

    @Mock
    ProductRepository productRepository;

    @Mock
    UserService userService;

    @Mock
    CategoryService categoryService;

    @Mock
    UploadService uploadService;

    @Mock
    ProductMapper productMapper;

    @InjectMocks
    ProductServiceImpl productService;

    User mockUser;

    CreateUpdateProductRequest productRequest;

    MultipartFile mockImageFile;

    @BeforeEach
    public void setup() {
        mockUser = User.builder()
                .id(UUID.randomUUID())
                .build();

        productRequest = CreateUpdateProductRequest.builder()
                .name("Test Product")
                .categoryId(UUID.randomUUID().toString())
                .build();

        mockImageFile = mock(MultipartFile.class);
    }

    @Test
    public void createProduct_categoryNotFound_throwException() {
        // Given
        when(userService.getUserFromToken()).thenReturn(mockUser);
        when(categoryService.getCategoryById(any(String.class)))
                .thenThrow(new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        // When
        AppException exception = assertThrows(AppException.class,
                () -> productService.createProduct(productRequest, mockImageFile));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CATEGORY_NOT_FOUND);
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.CATEGORY_NOT_FOUND.getMessage());
        verify(categoryService, times(1)).getCategoryById(any(String.class));
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    public void createProduct_uploadFileFailed_throwException() throws IOException {
        Category mockCategory = Category.builder()
                .name("test category")
                .description("Test description")
                .createdBy(mockUser.getId())
                .build();

        Product product = Product.builder().id(UUID.randomUUID()).build();

        when(userService.getUserFromToken()).thenReturn(mockUser);
        when(categoryService.getCategoryById(any(String.class))).thenReturn(mockCategory);
        when(productMapper.toProduct(any())).thenReturn(product);
        when(productRepository.save(any())).thenReturn(product);
        when(uploadService.uploadFile(any(), any(), any())).thenThrow(new IOException());


        assertThrows(IOException.class, () -> productService.createProduct(productRequest, mockImageFile));
        verify(productRepository, times(1)).save(any());
        verify(uploadService, times(1)).uploadFile(any(), any(), any());
    }

    @Test
    public void createProduct_validData_createProductSuccessfully() throws IOException {
        Category mockCategory = Category.builder()
                .id(UUID.randomUUID())
                .name("test category")
                .description("Test description")
                .createdBy(mockUser.getId())
                .build();

        productRequest.setCategoryId(mockCategory.getId().toString());

        Product mockProduct = Product.builder()
                .id(UUID.randomUUID())
                .name("Test Product")
                .build();

        when(userService.getUserFromToken()).thenReturn(mockUser);
        when(categoryService.getCategoryById(mockCategory.getId().toString())).thenReturn(mockCategory);
        when(productMapper.toProduct(any(CreateUpdateProductRequest.class))).thenReturn(mockProduct);
        when(uploadService.uploadFile(any(), eq("products"), anyString())).thenReturn("http://image.url");

        // When
        productService.createProduct(productRequest, mockImageFile);

        // Then
        verify(userService, times(1)).getUserFromToken();
        verify(categoryService, times(1)).getCategoryById(mockCategory.getId().toString());
        verify(productMapper, times(1)).toProduct(productRequest);
        verify(uploadService, times(1)).uploadFile(mockImageFile, "products", mockProduct.getId().toString());
        verify(productRepository, times(2)).save(mockProduct);
    }

    @Test
    public void getProductById_productNotFound_throwException() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class,
                () -> productService.getProductById(id.toString()));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND);
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
    }

    @Test
    public void getProductById_validId_returnProductDetailResponse() {
        Product mockProduct = Product.builder()
                .id(UUID.randomUUID())
                .name("mock product")
                .description("mock description")
                .price(BigDecimal.valueOf(10000))
                .build();

        ProductDetailResponse expectedResponse = ProductDetailResponse.builder()
                .id(mockProduct.getId().toString())
                .name(mockProduct.getName())
                .description(mockProduct.getDescription())
                .price(mockProduct.getPrice())
                .build();

        when(productRepository.findById(mockProduct.getId())).thenReturn(Optional.of(mockProduct));
        when(productMapper.toProductDetail(mockProduct)).thenReturn(expectedResponse);

        ProductDetailResponse result = productService.getProductById(mockProduct.getId().toString());

        assertNotNull(result);
        assertEquals(mockProduct.getId().toString(), result.getId());
        assertEquals(mockProduct.getName(), result.getName());
        assertEquals(mockProduct.getDescription(), result.getDescription());
        assertEquals(mockProduct.getPrice(), result.getPrice());

        verify(productRepository, times(1)).findById(mockProduct.getId());
        verify(productMapper, times(1)).toProductDetail(mockProduct);
    }

    @Test
    public void getProductsByIsDeleted_validInput_returnPageProductResponse() {
        //GIVEN
        boolean isDeleted = false;
        int page = 0;
        int size = 2;
        String sortBy = "name";
        String sortDir = "asc";

        Product product1 = Product.builder()
                .name("Product 1")
                .description("description 1")
                .build();

        Product product2 = Product.builder()
                .name("Product 2")
                .description("description 2")
                .build();

        ProductResponse productResponse1 = ProductResponse.builder()
                .name(product1.getName())
                .description(product1.getDescription())
                .build();

        ProductResponse productResponse2 = ProductResponse.builder()
                .name(product2.getName())
                .description(product2.getDescription())
                .build();


        Page<Product> productPage = new PageImpl<>(List.of(product1, product2));

        when(productRepository.findAllByIsDeleted(eq(isDeleted), any(Pageable.class)))
                .thenReturn(productPage);
        when(productMapper.toProductDTO(any())).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            if (p.getName().equals("Product 1")) return productResponse1;
            if (p.getName().equals("Product 2")) return productResponse2;
            return null;
        });

        //WHEN
        Page<ProductResponse> result = productService.getProductsByIsDeleted(isDeleted, page, size, sortBy, sortDir);

        //THEN
        assertEquals(2, result.getContent().size());
        assertEquals(product1.getName(), result.getContent().get(0).getName());
        assertEquals(product2.getName(), result.getContent().get(1).getName());

        verify(productRepository, times(1)).findAllByIsDeleted(eq(isDeleted), any(Pageable.class));
        verify(productMapper, times(2)).toProductDTO(any());
    }

}
