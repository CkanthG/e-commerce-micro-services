package com.sree.ecommerce.mappers;

import com.sree.ecommerce.entities.Category;
import com.sree.ecommerce.models.CategoryRequest;
import com.sree.ecommerce.models.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toCategory(CategoryRequest request) {
        return Category.builder()
                .id(request.id())
                .name(request.name())
                .description(request.description())
                .build();
    }

    public CategoryResponse toCategoryResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}
