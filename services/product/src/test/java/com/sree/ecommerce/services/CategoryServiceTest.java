package com.sree.ecommerce.services;

import com.sree.ecommerce.entities.Category;
import com.sree.ecommerce.exceptions.CategoryNotFoundException;
import com.sree.ecommerce.mappers.CategoryMapper;
import com.sree.ecommerce.models.CategoryRequest;
import com.sree.ecommerce.models.CategoryResponse;
import com.sree.ecommerce.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @InjectMocks
    private CategoryService service;
    @Mock
    private CategoryRepository repository;
    @Mock
    private CategoryMapper mapper;
    private CategoryRequest categoryRequest;
    private CategoryResponse categoryResponse;
    private Category category;
    private final String CATEGORY_NAME = "category-1";
    private final String CATEGORY_DESC = "category-1-desc";
    private final Integer CATEGORY_ID = 1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // given
        categoryRequest = new CategoryRequest(null, CATEGORY_NAME, CATEGORY_DESC);
        category = Category.builder().name(CATEGORY_NAME).description(CATEGORY_DESC).build();
        categoryResponse = new CategoryResponse(CATEGORY_ID, CATEGORY_NAME, CATEGORY_DESC);
    }

    @Test
    void createCategory_Success() {
        // when
        when(mapper.toCategory(categoryRequest)).thenReturn(category);
        when(repository.save(category)).thenReturn(category);
        when(mapper.toCategoryResponse(category)).thenReturn(categoryResponse);
        // then
        var actual = service.createCategory(categoryRequest);
        assertEquals(categoryRequest.name(), actual.name());
        assertEquals(categoryRequest.description(), actual.description());
        // verify
        verify(mapper, times(1)).toCategory(categoryRequest);
        verify(repository, times(1)).save(category);
        verify(mapper, times(1)).toCategoryResponse(category);
    }

    @Test
    void updateCategory_Success() {
        // given
        categoryRequest = new CategoryRequest(CATEGORY_ID, CATEGORY_NAME + "-updated", CATEGORY_DESC + "-updated");
        categoryResponse = new CategoryResponse(CATEGORY_ID, CATEGORY_NAME + "-updated", CATEGORY_DESC + "-updated");
        // when
        when(repository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        when(mapper.toCategory(categoryRequest)).thenReturn(category);
        when(repository.save(category)).thenReturn(category);
        when(mapper.toCategoryResponse(category)).thenReturn(categoryResponse);
        // then
        var actual = service.updateCategory(categoryRequest);
        assertEquals(categoryRequest.id(), actual.id());
        assertEquals(categoryRequest.name(), actual.name());
        assertEquals(categoryRequest.description(), actual.description());
        // verify
        verify(repository, times(1)).findById(CATEGORY_ID);
        verify(mapper, times(1)).toCategory(categoryRequest);
        verify(repository, times(1)).save(category);
        verify(mapper, times(1)).toCategoryResponse(category);
    }

    @Test
    void updateCategory_ThrowsCategoryNotFoundException() {
        assertThrows(
                CategoryNotFoundException.class,
                () -> service.updateCategory(categoryRequest)
        );
    }

    @Test
    void findAllCategories_Success() {
        // given
        category.setId(CATEGORY_ID);
        var list = List.of(category);
        // when
        when(repository.findAll()).thenReturn(list);
        when(mapper.toCategoryResponse(category)).thenReturn(categoryResponse);
        // then
        var actual = service.findAllCategories();
        assertEquals(list.size(), actual.size());
        var actualCategoryResponse = actual.getFirst();
        assertEquals(category.getId(), actualCategoryResponse.id());
        assertEquals(category.getName(), actualCategoryResponse.name());
        assertEquals(category.getDescription(), actualCategoryResponse.description());
        // verify
        verify(repository, times(1)).findAll();
        verify(mapper, times(1)).toCategoryResponse(category);
    }

    @Test
    void findCategoryById_Success() {
        // when
        when(repository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        when(mapper.toCategoryResponse(category)).thenReturn(categoryResponse);
        // then
        var actual = service.findCategoryById(CATEGORY_ID);
        assertEquals(CATEGORY_ID, actual.id());
        assertNotNull(actual.name());
        assertNotNull(actual.description());
        // verify
        verify(repository, times(1)).findById(CATEGORY_ID);
        verify(mapper, times(1)).toCategoryResponse(category);
    }

    @Test
    void findCategoryById_ThrowsCategoryNotFoundException() {
        assertThrows(
                CategoryNotFoundException.class,
                () -> service.findCategoryById(CATEGORY_ID)
        );
    }

    @Test
    void deleteCategoryById_Success() {
        // when
        doNothing().when(repository).deleteById(CATEGORY_ID);
        when(repository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        // then
        service.deleteCategoryById(CATEGORY_ID);
        // verify
        verify(repository, times(1)).deleteById(CATEGORY_ID);
    }

    @Test
    void deleteCategoryById_ThrowsCategoryNotFoundException() {
        assertThrows(
                CategoryNotFoundException.class,
                () -> service.deleteCategoryById(CATEGORY_ID)
        );
    }
}