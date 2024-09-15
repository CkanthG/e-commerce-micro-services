package com.sree.ecommerce.services;

import com.sree.ecommerce.entities.Product;
import com.sree.ecommerce.exceptions.ProductNotFoundException;
import com.sree.ecommerce.exceptions.ProductPurchaseException;
import com.sree.ecommerce.mappers.ProductMapper;
import com.sree.ecommerce.models.ProductPurchaseRequest;
import com.sree.ecommerce.models.ProductPurchaseResponse;
import com.sree.ecommerce.models.ProductRequest;
import com.sree.ecommerce.models.ProductResponse;
import com.sree.ecommerce.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    public ProductResponse createProduct(ProductRequest request) {
        var product = productRepository.save(mapper.toProduct(request));
        return mapper.toProductResponse(product);
    }

    public ProductResponse updateProduct(ProductRequest request) {
        getProduct(request.id());
        var product = productRepository.save(mapper.toProduct(request));
        return mapper.toProductResponse(product);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(mapper::toProductResponse).toList();
    }

    public ProductResponse getProductById(Integer productId) {
        var product = getProduct(productId);
        return mapper.toProductResponse(product);
    }

    private Product getProduct(Integer productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException(format("No Product found with specified ID : %s", productId))
        );
    }

    public void deleteProductById(Integer productId) {
        getProduct(productId);
        productRepository.deleteById(productId);
    }

    public List<ProductPurchaseResponse> productPurchase(List<ProductPurchaseRequest> requests) {
        var productIds = requests
                .stream()
                .map(ProductPurchaseRequest::productId)
                .toList();
        var storedProducts = productRepository.findAllByIdInOrderById(productIds);
        if (productIds.size() != storedProducts.size()) {
            throw new ProductPurchaseException("One or more products are not available.");
        }
        var storedRequests = requests
                .stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();
        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();
        for (int i = 0; i < storedProducts.size(); i ++) {
            var product = storedProducts.get(i);
            var productRequest = storedRequests.get(i);
            if (product.getAvailableQuantity() < productRequest.quantity()) {
                throw new ProductPurchaseException(
                        format("Insufficient stock quantity for product with ID : %s", productRequest.productId())
                );
            }
            var updatedQuantity = product.getAvailableQuantity() - productRequest.quantity();
            product.setAvailableQuantity(updatedQuantity);
            productRepository.save(product);
            purchasedProducts.add(mapper.toProductPurchaseResponse(product, productRequest.quantity()));
        }
        return purchasedProducts;
    }
}
