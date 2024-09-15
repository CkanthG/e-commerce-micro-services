package com.sree.ecommerce.controllers;

import com.sree.ecommerce.models.CategoryRequest;
import com.sree.ecommerce.models.CategoryResponse;
import com.sree.ecommerce.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody @Valid CategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createCategory(request));
    }

    @PutMapping
    public ResponseEntity<CategoryResponse> update(@RequestBody @Valid CategoryRequest request) {
        return ResponseEntity.ok(service.updateCategory(request));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAll() {
        return ResponseEntity.ok(service.findAllCategories());
    }

    @GetMapping("/{category-id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable(name = "category-id") Integer categoryId) {
        return ResponseEntity.ok(service.findCategoryById(categoryId));
    }

    @DeleteMapping("/{category-id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "category-id") Integer categoryId) {
        service.deleteCategoryById(categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
