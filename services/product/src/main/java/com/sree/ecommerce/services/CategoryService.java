package com.sree.ecommerce.services;

import com.sree.ecommerce.entities.Category;
import com.sree.ecommerce.exceptions.CategoryNotFoundException;
import com.sree.ecommerce.mappers.CategoryMapper;
import com.sree.ecommerce.models.CategoryRequest;
import com.sree.ecommerce.models.CategoryResponse;
import com.sree.ecommerce.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;

    public CategoryResponse createCategory(CategoryRequest request) {
        var category = categoryRepository.save(mapper.toCategory(request));
        return mapper.toCategoryResponse(category);
    }

    public CategoryResponse updateCategory(CategoryRequest request) {
        getCategory(request.id());
        var category = categoryRepository.save(mapper.toCategory(request));
        return mapper.toCategoryResponse(category);
    }

    public List<CategoryResponse> findAllCategories() {
        return categoryRepository.findAll().stream().map(mapper::toCategoryResponse).toList();
    }

    public CategoryResponse findCategoryById(Integer categoryId) {
        var category = getCategory(categoryId);
        return mapper.toCategoryResponse(category);
    }

    private Category getCategory(Integer categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new CategoryNotFoundException(format("No category found with specified ID : %s", categoryId))
        );
    }

    public void deleteCategoryById(Integer categoryId) {
        getCategory(categoryId);
        categoryRepository.deleteById(categoryId);
    }
}
