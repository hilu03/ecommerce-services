package com.rookies.ecommerce.service;

import com.rookies.ecommerce.dto.request.CreateUpdateCategoryRequest;
import com.rookies.ecommerce.entity.Category;
import com.rookies.ecommerce.entity.User;
import com.rookies.ecommerce.exception.AppException;
import com.rookies.ecommerce.exception.ErrorCode;
import com.rookies.ecommerce.repository.CategoryRepository;
import com.rookies.ecommerce.service.category.CategoryServiceImpl;
import com.rookies.ecommerce.service.user.UserService;
import com.rookies.ecommerce.utils.SlugUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryServiceUnitTests {

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    UserService userService;

    @InjectMocks
    CategoryServiceImpl categoryService;

    User mockUser;

    @BeforeEach
    public void setup() {
        mockUser = User.builder()
                .id(UUID.randomUUID())
                .build();
    }

    @Test
    public void createCategory_categoryNameAlreadyExists_throwException() {
        CreateUpdateCategoryRequest request = CreateUpdateCategoryRequest.builder()
                .name("existed category name")
                .description("This is a test category")
                .build();

        when(userService.getUserFromToken()).thenReturn(mockUser);
        when(categoryRepository.save(any(Category.class)))
                .thenThrow(new DataIntegrityViolationException(ErrorCode.CATEGORY_NAME_EXISTED.getMessage()));

        Exception exception = assertThrows(DataIntegrityViolationException.class,
                () -> categoryService.createCategory(request));

        assertThat(exception.getMessage()).contains(ErrorCode.CATEGORY_NAME_EXISTED.getMessage());
    }

    @Test
    public void createCategory_validData_returnCategory() {
        //Given
        CreateUpdateCategoryRequest request = CreateUpdateCategoryRequest.builder()
                .name("test category name")
                .description("This is a test category")
                .build();

        Category expectedCategory = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .slug(SlugUtil.createSlug(request.getName()))
                .build();

        expectedCategory.setCreatedBy(mockUser.getId());
        expectedCategory.setModifiedBy(mockUser.getId());

        when(userService.getUserFromToken()).thenReturn(mockUser);
        when(categoryRepository.save(any(Category.class))).thenReturn(expectedCategory);

        //When
        Category result = categoryService.createCategory(request);

        //Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(request.getName());
        assertThat(result.getDescription()).isEqualTo(request.getDescription());
        assertThat(result.getSlug()).isEqualTo(SlugUtil.createSlug(request.getName()));
        assertThat(result.getCreatedBy()).isEqualTo(mockUser.getId());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void getCategoryById_categoryNotFound_throwException() {
        UUID id = UUID.randomUUID();
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class,
                () -> categoryService.getCategoryById(id.toString()));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CATEGORY_NOT_FOUND);
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.CATEGORY_NOT_FOUND.getMessage());
    }

    @Test
    public void getCategoryById_validId_returnCategory() {
        UUID categoryId = UUID.randomUUID();
        String categoryIdStr = categoryId.toString();

        Category mockCategory = Category.builder()
                .id(categoryId)
                .name("test category")
                .description("Test description")
                .createdBy(mockUser.getId())
                .build();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(mockCategory));

        // When
        Category result = categoryService.getCategoryById(categoryIdStr);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(categoryId);
        assertThat(result.getName()).isEqualTo(mockCategory.getName());
        assertThat(result.getDescription()).isEqualTo(mockCategory.getDescription());
        assertThat(result.getCreatedBy()).isEqualTo(mockCategory.getCreatedBy());
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    public void getActiveCategories_returnListOfActiveCategories() {
        // Given
        List<Category> mockCategories = List.of(
                Category.builder().name("Category A").isDeleted(false).build(),
                Category.builder().name("Category B").isDeleted(false).build()
        );

        when(categoryRepository.findAllByIsDeletedOrderByNameAsc(false)).thenReturn(mockCategories);

        // When
        List<Category> result = categoryService.getActiveCategories();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Category A");
        verify(categoryRepository, times(1)).findAllByIsDeletedOrderByNameAsc(false);
    }

}
