package com.sree.ecommerce.controllers;

import com.sree.ecommerce.models.ProductRequest;
import com.sree.ecommerce.models.ProductResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ProductControllerTest extends AbstractPostgreSQLContainer{

    @Autowired
    TestRestTemplate restTemplate;
    private ProductRequest productRequest;
    private String PRODUCT_BASE_URL;
    @LocalServerPort
    private int port;
    private final String PRODUCT_NAME = "product-1";
    private final String PRODUCT_DESC = "product-1-desc";
    private final double PRODUCT_AVAILABLE_QUANTITY = 1.00;
    private final BigDecimal PRODUCT_PRICE = BigDecimal.valueOf(1.00);
    private final Integer PRODUCT_CATEGORY = 1;

    @BeforeEach
    void setUp() {
        PRODUCT_BASE_URL = "http://localhost:" + port + "/api/v1/products";
        productRequest = new ProductRequest(
                null,
                PRODUCT_NAME,
                PRODUCT_DESC,
                PRODUCT_AVAILABLE_QUANTITY,
                PRODUCT_PRICE,
                PRODUCT_CATEGORY
        );
    }

    @Test
    void testCreate_Success() {
        ResponseEntity<ProductResponse> responseEntity = restTemplate.postForEntity(
                PRODUCT_BASE_URL,
                productRequest,
                ProductResponse.class
        );
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        ProductResponse productResponse = responseEntity.getBody();
        assert productResponse != null;
        assertEquals(productRequest.name(), productResponse.name());
        assertEquals(productRequest.description(), productResponse.description());
        assertEquals(productRequest.availableQuantity(), productResponse.availableQuantity());
        assertEquals(productRequest.categoryId(), productResponse.categoryId());
        assertEquals(0, productRequest.price().compareTo(productResponse.price()));
    }

    @Test
    void testCreate_Failed() {
        ResponseEntity<ProductResponse> responseEntity = restTemplate.postForEntity(
                PRODUCT_BASE_URL,
                new ProductRequest(null, null, null, 0.0, null, null),
                ProductResponse.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testUpdate_Success() {
        ResponseEntity<ProductResponse> responseEntity = restTemplate.postForEntity(
                PRODUCT_BASE_URL,
                productRequest,
                ProductResponse.class
        );
        ProductResponse response = responseEntity.getBody();
        assert response != null;
        productRequest = new ProductRequest(
                response.id(),
                PRODUCT_NAME + "-updated",
                PRODUCT_DESC + "-updated",
                PRODUCT_AVAILABLE_QUANTITY,
                PRODUCT_PRICE,
                PRODUCT_CATEGORY
        );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<ProductRequest> entity = new HttpEntity<>(productRequest, httpHeaders);
        ResponseEntity<ProductResponse> productUpdatedResponse = restTemplate.exchange(
                PRODUCT_BASE_URL,
                HttpMethod.PUT,
                entity,
                ProductResponse.class
        );
        assertEquals(HttpStatus.OK, productUpdatedResponse.getStatusCode());
        ProductResponse body = productUpdatedResponse.getBody();
        assert body != null;
        assertEquals(response.id(), body.id());
        assertEquals(productRequest.name(), body.name());
        assertEquals(productRequest.description(), body.description());
        assertEquals(productRequest.availableQuantity(), body.availableQuantity());
        assertEquals(productRequest.categoryId(), body.categoryId());
        assertEquals(0, productRequest.price().compareTo(body.price()));
    }

    @Test
    void testUpdate_Failed() {
        productRequest = new ProductRequest(
                99999,
                PRODUCT_NAME + "-updated",
                PRODUCT_DESC + "-updated",
                PRODUCT_AVAILABLE_QUANTITY,
                PRODUCT_PRICE,
                PRODUCT_CATEGORY
        );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<ProductRequest> entity = new HttpEntity<>(productRequest, httpHeaders);
        ResponseEntity<ProductResponse> productUpdatedResponse = restTemplate.exchange(
                PRODUCT_BASE_URL,
                HttpMethod.PUT,
                entity,
                ProductResponse.class
        );
        assertEquals(HttpStatus.NOT_FOUND, productUpdatedResponse.getStatusCode());
    }

    @Test
    void testFindAll_Success() {
        restTemplate.postForEntity(
                PRODUCT_BASE_URL,
                productRequest,
                ProductResponse.class
        );
        ResponseEntity<ProductResponse[]> getAllProducts = restTemplate.getForEntity(PRODUCT_BASE_URL, ProductResponse[].class);
        int size = Objects.requireNonNull(getAllProducts.getBody()).length;
        ProductResponse productResponse = getAllProducts.getBody()[size - 1];
        assertEquals(productRequest.name(), productResponse.name());
        assertEquals(productRequest.description(), productResponse.description());
        assertEquals(productRequest.availableQuantity(), productResponse.availableQuantity());
        assertEquals(productRequest.categoryId(), productResponse.categoryId());
        assertEquals(0, productRequest.price().compareTo(productResponse.price()));
    }

    @Test
    void findById_Success() {
        ResponseEntity<ProductResponse> responseEntity = restTemplate.postForEntity(
                PRODUCT_BASE_URL,
                productRequest,
                ProductResponse.class
        );
        ProductResponse response = responseEntity.getBody();
        assert response != null;
        ResponseEntity<ProductResponse> productResponseResponseEntity = restTemplate.getForEntity(
                PRODUCT_BASE_URL + "/" + response.id(),
                ProductResponse.class
        );
        assertEquals(HttpStatus.OK, productResponseResponseEntity.getStatusCode());
        ProductResponse productResponse = productResponseResponseEntity.getBody();
        assert productResponse != null;
        assertEquals(response.id(), productResponse.id());
        assertEquals(response.name(), productResponse.name());
        assertEquals(response.description(), productResponse.description());
        assertEquals(response.availableQuantity(), productResponse.availableQuantity());
        assertEquals(0, productRequest.price().compareTo(productResponse.price()));
        assertEquals(response.categoryId(), productResponse.categoryId());
    }

    @Test
    void findById_Failed() {
        ResponseEntity<ProductResponse> productResponseResponseEntity = restTemplate.getForEntity(
                PRODUCT_BASE_URL + "/9999",
                ProductResponse.class
        );
        assertEquals(HttpStatus.NOT_FOUND, productResponseResponseEntity.getStatusCode());
    }

    @Test
    void findDelete_Success() {
        ResponseEntity<ProductResponse> responseEntity = restTemplate.postForEntity(
                PRODUCT_BASE_URL,
                productRequest,
                ProductResponse.class
        );
        ProductResponse response = responseEntity.getBody();
        assert response != null;
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                PRODUCT_BASE_URL + "/" + response.id(),
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
    }

    @Test
    void findDelete_Failed() {
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                PRODUCT_BASE_URL + "/99999",
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertEquals(HttpStatus.NOT_FOUND, deleteResponse.getStatusCode());
    }
}