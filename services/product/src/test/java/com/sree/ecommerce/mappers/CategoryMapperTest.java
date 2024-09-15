package com.sree.ecommerce.mappers;

import com.sree.ecommerce.entities.Category;
import com.sree.ecommerce.models.CategoryRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryMapperTest {

    private CategoryMapper mapper;
    private CategoryRequest categoryRequest;
    private Category category;
    private final String CATEGORY_NAME = "category-1";
    private final String CATEGORY_DESC = "category-1-desc";
    private final Integer CATEGORY_ID = 1;

    @BeforeEach
    void setUp() {
        mapper = new CategoryMapper();
        // given
        categoryRequest = new CategoryRequest(CATEGORY_ID, CATEGORY_NAME, CATEGORY_DESC);
        category = Category.builder().id(CATEGORY_ID).name(CATEGORY_NAME).description(CATEGORY_DESC).build();
    }

    @Test
    void toCategory_Success() {
        var actual = mapper.toCategory(categoryRequest);
        assertEquals(categoryRequest.id(), actual.getId());
        assertEquals(categoryRequest.name(), actual.getName());
        assertEquals(categoryRequest.description(), actual.getDescription());
    }

    @Test
    void toCategoryResponse_Success() {
        var actual = mapper.toCategoryResponse(category);
        assertEquals(category.getId(), actual.id());
        assertEquals(category.getName(), actual.name());
        assertEquals(category.getDescription(), actual.description());
    }
}