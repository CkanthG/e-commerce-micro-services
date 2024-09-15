package com.sree.ecommerce.mappers;

import com.sree.ecommerce.entities.Category;
import com.sree.ecommerce.entities.Product;
import com.sree.ecommerce.models.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private ProductMapper mapper;
    private Product product;
    private ProductRequest productRequest;
    private static final Integer PRODUCT_ID = 1;
    private final String PRODUCT_NAME = "product-1";
    private final String PRODUCT_DESC = "product-1-desc";
    private final double PRODUCT_AVAILABLE_QUANTITY = 1.00;
    private final BigDecimal PRODUCT_PRICE = BigDecimal.valueOf(1.00);
    private final Integer PRODUCT_CATEGORY = 1;

    @BeforeEach
    void setUp() {
        mapper = new ProductMapper();
        productRequest = new ProductRequest(
                PRODUCT_ID,
                PRODUCT_NAME,
                PRODUCT_DESC,
                PRODUCT_AVAILABLE_QUANTITY,
                PRODUCT_PRICE,
                PRODUCT_CATEGORY
        );
        product = Product.builder()
                .id(PRODUCT_ID)
                .name(PRODUCT_NAME)
                .description(PRODUCT_DESC)
                .availableQuantity(PRODUCT_AVAILABLE_QUANTITY)
                .price(PRODUCT_PRICE)
                .category(
                        Category.builder()
                                .id(PRODUCT_CATEGORY)
                                .build()
                )
                .build();
    }

    @Test
    void toProduct_Success() {
        var actual = mapper.toProduct(productRequest);
        assertEquals(productRequest.id(), actual.getId());
        assertEquals(productRequest.name(), actual.getName());
        assertEquals(productRequest.description(), actual.getDescription());
        assertEquals(productRequest.availableQuantity(), actual.getAvailableQuantity());
        assertEquals(productRequest.price(), actual.getPrice());
        assertEquals(productRequest.categoryId(), actual.getCategory().getId());
    }

    @Test
    void toProductResponse_Success() {
        var actual = mapper.toProductResponse(product);
        assertEquals(product.getId(), actual.id());
        assertEquals(product.getName(), actual.name());
        assertEquals(product.getDescription(), actual.description());
        assertEquals(product.getAvailableQuantity(), actual.availableQuantity());
        assertEquals(product.getPrice(), actual.price());
        assertEquals(product.getCategory().getId(), actual.categoryId());
    }

    @Test
    void toProductPurchaseResponse_Success() {
        var actual = mapper.toProductPurchaseResponse(product, 1);
        assertEquals(product.getId(), actual.productId());
        assertEquals(product.getName(), actual.name());
        assertEquals(product.getDescription(), actual.description());
        assertEquals(product.getAvailableQuantity(), actual.quantity());
        assertEquals(product.getPrice(), actual.price());
    }
}