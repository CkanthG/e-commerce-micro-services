package com.sree.ecommerce.services;

import com.sree.ecommerce.entities.Category;
import com.sree.ecommerce.entities.Product;
import com.sree.ecommerce.exceptions.ProductNotFoundException;
import com.sree.ecommerce.exceptions.ProductPurchaseException;
import com.sree.ecommerce.mappers.ProductMapper;
import com.sree.ecommerce.models.ProductPurchaseRequest;
import com.sree.ecommerce.models.ProductPurchaseResponse;
import com.sree.ecommerce.models.ProductRequest;
import com.sree.ecommerce.models.ProductResponse;
import com.sree.ecommerce.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @InjectMocks
    private ProductService service;
    @Mock
    private ProductRepository repository;
    @Mock
    private ProductMapper mapper;
    private ProductRequest productRequest;
    private ProductResponse productResponse;
    private Product product;
    private static final Integer PRODUCT_ID = 1;
    private final String PRODUCT_NAME = "product-1";
    private final String PRODUCT_DESC = "product-1-desc";
    private final double PRODUCT_AVAILABLE_QUANTITY = 1.00;
    private final BigDecimal PRODUCT_PRICE = BigDecimal.valueOf(1.00);
    private final Integer PRODUCT_CATEGORY = 1;
    private final String PRODUCT_CATEGORY_NAME = "Category-1";
    private final String PRODUCT_CATEGORY_DESC = "Category-1-desc";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // given
        productRequest = new ProductRequest(
                null,
                PRODUCT_NAME,
                PRODUCT_DESC,
                PRODUCT_AVAILABLE_QUANTITY,
                PRODUCT_PRICE,
                PRODUCT_CATEGORY
        );
        productResponse = new ProductResponse(
                PRODUCT_ID,
                PRODUCT_NAME,
                PRODUCT_DESC,
                PRODUCT_AVAILABLE_QUANTITY,
                PRODUCT_PRICE,
                PRODUCT_CATEGORY,
                PRODUCT_CATEGORY_NAME,
                PRODUCT_CATEGORY_DESC
        );
        product = Product.builder()
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
    void createProduct_Success() {
        // when
        when(mapper.toProduct(productRequest)).thenReturn(product);
        when(repository.save(product)).thenReturn(product);
        when(mapper.toProductResponse(product)).thenReturn(productResponse);
        // then
        var actual = service.createProduct(productRequest);
        assertEquals(productRequest.name(), actual.name());
        assertEquals(productRequest.description(), actual.description());
        assertEquals(productRequest.availableQuantity(), actual.availableQuantity());
        assertEquals(productRequest.price(), actual.price());
        assertEquals(productRequest.categoryId(), actual.categoryId());
        // verify
        verify(mapper, times(1)).toProduct(productRequest);
        verify(repository, times(1)).save(product);
        verify(mapper, times(1)).toProductResponse(product);
    }

    @Test
    void updateProduct_Success() {
        // given
        product.setId(PRODUCT_ID);
        productRequest = new ProductRequest(
                PRODUCT_ID,
                PRODUCT_NAME,
                PRODUCT_DESC,
                PRODUCT_AVAILABLE_QUANTITY,
                PRODUCT_PRICE,
                PRODUCT_CATEGORY
        );
        // when
        when(repository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(mapper.toProduct(productRequest)).thenReturn(product);
        when(repository.save(product)).thenReturn(product);
        when(mapper.toProductResponse(product)).thenReturn(productResponse);
        // then
        var actual = service.updateProduct(productRequest);
        assertEquals(productRequest.id(), actual.id());
        assertEquals(productRequest.name(), actual.name());
        assertEquals(productRequest.name(), actual.name());
        assertEquals(productRequest.description(), actual.description());
        assertEquals(productRequest.availableQuantity(), actual.availableQuantity());
        assertEquals(productRequest.price(), actual.price());
        assertEquals(productRequest.categoryId(), actual.categoryId());
        // verify
        verify(repository, times(1)).findById(PRODUCT_ID);
        verify(mapper, times(1)).toProduct(productRequest);
        verify(repository, times(1)).save(product);
        verify(mapper, times(1)).toProductResponse(product);
    }

    @Test
    void testUpdateProduct_ThrowsProductNotFoundException() {
        assertThrows(
                ProductNotFoundException.class,
                () -> service.updateProduct(productRequest)
        );
    }

    @Test
    void getAllProducts_Success() {
        // given
        product.setId(PRODUCT_ID);
        var list = List.of(product);
        // when
        when(repository.findAll()).thenReturn(list);
        when(mapper.toProductResponse(product)).thenReturn(productResponse);
        // then
        var actual = service.getAllProducts();
        assert actual != null;
        var actualProductResponse = actual.getFirst();
        assertEquals(product.getId(), actualProductResponse.id());
        assertEquals(product.getName(), actualProductResponse.name());
        assertEquals(product.getDescription(), actualProductResponse.description());
        assertEquals(product.getAvailableQuantity(), actualProductResponse.availableQuantity());
        assertEquals(product.getPrice(), actualProductResponse.price());
        assertNotNull(actualProductResponse.categoryId());
        // verify
        verify(repository, times(1)).findAll();
        verify(mapper, times(1)).toProductResponse(product);
    }

    @Test
    void getProductById_Success() {
        // given
        product.setId(PRODUCT_ID);
        // when
        when(repository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(mapper.toProductResponse(product)).thenReturn(productResponse);
        // then
        var actual = service.getProductById(PRODUCT_ID);
        assertEquals(product.getId(), actual.id());
        assertEquals(product.getName(), actual.name());
        assertEquals(product.getDescription(), actual.description());
        assertEquals(product.getAvailableQuantity(), actual.availableQuantity());
        assertEquals(product.getPrice(), actual.price());
        assertEquals(product.getCategory().getId(), actual.categoryId());
        // verify
        verify(repository, times(1)).findById(PRODUCT_ID);
        verify(mapper, times(1)).toProductResponse(product);
    }

    @Test
    void getProductById_ThrowsProductNotFoundException() {
        assertThrows(
                ProductNotFoundException.class,
                () -> service.getProductById(PRODUCT_ID)
        );
    }

    @Test
    void deleteProductById_Success() {
        // when
        when(repository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        doNothing().when(repository).deleteById(PRODUCT_ID);
        // then
        service.deleteProductById(PRODUCT_ID);
        // verify
        verify(repository, times(1)).findById(PRODUCT_ID);
        verify(repository, times(1)).deleteById(PRODUCT_ID);
    }

    @Test
    void deleteProductById_ThrowsProductNotFoundException() {
        assertThrows(
                ProductNotFoundException.class,
                () -> service.deleteProductById(PRODUCT_ID)
        );
    }

    @Test
    void productPurchase_Success() {
        // given
        var productPurchaseRequest = new ProductPurchaseRequest(PRODUCT_ID, 10);
        var list = List.of(productPurchaseRequest);
        product.setId(PRODUCT_ID);
        product.setAvailableQuantity(10);
        var productsList = List.of(product);
        // when
        when(repository.findAllByIdInOrderById(List.of(PRODUCT_ID))).thenReturn(productsList);
        when(repository.save(product)).thenReturn(product);
        when(mapper.toProductPurchaseResponse(product, 10)).thenReturn(new ProductPurchaseResponse(
                PRODUCT_ID,
                PRODUCT_NAME,
                PRODUCT_DESC,
                PRODUCT_PRICE,
                10
        ));
        // then
        var actual = service.productPurchase(list);
        assertEquals(list.size(), actual.size());
        // verify
        verify(repository, times(1)).findAllByIdInOrderById(List.of(PRODUCT_ID));
        verify(repository, times(1)).save(product);
        verify(mapper, times(1)).toProductPurchaseResponse(product, 10);
    }

    @Test
    void productPurchase_ThrowProductPurchaseException() {
        // given
        var productPurchaseRequest = new ProductPurchaseRequest(PRODUCT_ID, 10);
        var list = List.of(productPurchaseRequest);
        product.setId(PRODUCT_ID);
        var productsList = List.of(product, product);
        // when
        when(repository.findAllByIdInOrderById(List.of(PRODUCT_ID))).thenReturn(productsList);
        assertThrows(
                ProductPurchaseException.class,
                () -> service.productPurchase(list)
        );
    }

    @Test
    void productPurchase_ThrowsProductPurchaseException() {
        // given
        var productPurchaseRequest = new ProductPurchaseRequest(PRODUCT_ID, 10);
        var list = List.of(productPurchaseRequest);
        product.setId(PRODUCT_ID);
        var productsList = List.of(product);
        // when
        when(repository.findAllByIdInOrderById(List.of(PRODUCT_ID))).thenReturn(productsList);
        when(repository.save(product)).thenReturn(product);
        when(mapper.toProductPurchaseResponse(product, 10)).thenReturn(new ProductPurchaseResponse(
                PRODUCT_ID,
                PRODUCT_NAME,
                PRODUCT_DESC,
                PRODUCT_PRICE,
                10
        ));
        assertThrows(
                ProductPurchaseException.class,
                () -> service.productPurchase(list)
        );
    }
}