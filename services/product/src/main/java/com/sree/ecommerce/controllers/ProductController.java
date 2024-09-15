package com.sree.ecommerce.controllers;

import com.sree.ecommerce.models.ProductPurchaseRequest;
import com.sree.ecommerce.models.ProductPurchaseResponse;
import com.sree.ecommerce.models.ProductRequest;
import com.sree.ecommerce.models.ProductResponse;
import com.sree.ecommerce.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody @Valid ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createProduct(request));
    }

    @PostMapping("/purchase")
    public ResponseEntity<List<ProductPurchaseResponse>> purchase(@RequestBody @Valid List<ProductPurchaseRequest> requests) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.productPurchase(requests));
    }

    @PutMapping
    public ResponseEntity<ProductResponse> update(@RequestBody @Valid ProductRequest request) {
        return ResponseEntity.ok(service.updateProduct(request));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        return ResponseEntity.ok(service.getAllProducts());
    }

    @GetMapping("/{product-id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable(name = "product-id") Integer productId) {
        return ResponseEntity.ok(service.getProductById(productId));
    }

    @DeleteMapping("/{product-id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "product-id") Integer productId) {
        service.deleteProductById(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
