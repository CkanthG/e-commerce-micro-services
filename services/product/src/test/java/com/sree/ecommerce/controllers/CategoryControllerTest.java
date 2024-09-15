package com.sree.ecommerce.controllers;

import com.sree.ecommerce.models.CategoryRequest;
import com.sree.ecommerce.models.CategoryResponse;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CategoryControllerTest extends AbstractPostgreSQLContainer{

    @Autowired
    TestRestTemplate restTemplate;
    private CategoryRequest categoryRequest;
    private String CATEGORY_BASE_URL;
    @LocalServerPort
    private int port;
    private final String CATEGORY_NAME = "category-1";
    private final String CATEGORY_DESC = "category-1-desc";

    @BeforeEach
    void setUp() {
        CATEGORY_BASE_URL = "http://localhost:" + port + "/api/v1/category";
        categoryRequest = new CategoryRequest(null, CATEGORY_NAME, CATEGORY_DESC);
    }

    @Test
    void testCreate_Success() {
        ResponseEntity<CategoryResponse> responseEntity = restTemplate.postForEntity(
                CATEGORY_BASE_URL,
                categoryRequest,
                CategoryResponse.class
        );
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        var response = responseEntity.getBody();
        assert response != null;
        assertEquals(categoryRequest.name(), response.name());
        assertEquals(categoryRequest.description(), response.description());
    }

    @Test
    void testCreate_Failed() {
        var response = restTemplate.postForEntity(
                CATEGORY_BASE_URL,
                new CategoryRequest(
                        null,
                        null,
                        null
                ),
                CategoryResponse.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUpdate_Success() {
        ResponseEntity<CategoryResponse> category = restTemplate.postForEntity(
                CATEGORY_BASE_URL,
                categoryRequest,
                CategoryResponse.class
        );
        categoryRequest = new CategoryRequest(
                Objects.requireNonNull(category.getBody()).id(),
                CATEGORY_NAME + "-updated",
                CATEGORY_DESC + "-updated"
        );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<CategoryRequest> entity = new HttpEntity<>(categoryRequest, httpHeaders);
        ResponseEntity<CategoryResponse> responseEntity = restTemplate.exchange(
                CATEGORY_BASE_URL,
                HttpMethod.PUT,
                entity,
                CategoryResponse.class
        );
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        var response = responseEntity.getBody();
        assert response != null;
        assertEquals(categoryRequest.id(), response.id());
        assertEquals(categoryRequest.name(), response.name());
        assertEquals(categoryRequest.description(), response.description());
    }

    @Test
    void testUpdate_Failed() {
        categoryRequest = new CategoryRequest(
                99999,
                CATEGORY_NAME + "-updated",
                CATEGORY_DESC + "-updated"
        );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<CategoryRequest> entity = new HttpEntity<>(categoryRequest, httpHeaders);
        ResponseEntity<CategoryResponse> responseEntity = restTemplate.exchange(
                CATEGORY_BASE_URL,
                HttpMethod.PUT,
                entity,
                CategoryResponse.class
        );
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testFindAll_Success() {
        restTemplate.postForEntity(CATEGORY_BASE_URL, categoryRequest, CategoryResponse.class);
        ResponseEntity<CategoryResponse[]> getAllCategories = restTemplate.getForEntity(CATEGORY_BASE_URL, CategoryResponse[].class);
        assertEquals(HttpStatus.OK, getAllCategories.getStatusCode());
        int size = Objects.requireNonNull(getAllCategories.getBody()).length;
        CategoryResponse categoryResponse = getAllCategories.getBody()[size - 1];
        assertEquals(categoryRequest.name(), categoryResponse.name());
        assertEquals(categoryRequest.description(), categoryResponse.description());
    }

    @Test
    void testFindById_Success() {
        ResponseEntity<CategoryResponse> categoryResponse = restTemplate.postForEntity(CATEGORY_BASE_URL, categoryRequest, CategoryResponse.class);
        CategoryResponse category = categoryResponse.getBody();
        Integer categoryId = Objects.requireNonNull(category).id();
        ResponseEntity<CategoryResponse> responseEntity = restTemplate.getForEntity(
                CATEGORY_BASE_URL + "/" + categoryId,
                CategoryResponse.class
        );
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(category.name(), Objects.requireNonNull(responseEntity.getBody()).name());
        assertEquals(category.id(), responseEntity.getBody().id());
        assertEquals(category.description(), responseEntity.getBody().description());
    }

    @Test
    void testFindById_Failed() {
        ResponseEntity<CategoryResponse> responseEntity = restTemplate.getForEntity(
                CATEGORY_BASE_URL + "/99999",
                CategoryResponse.class
        );
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testDelete_Success() {
        ResponseEntity<CategoryResponse> categoryResponse = restTemplate.postForEntity(CATEGORY_BASE_URL, categoryRequest, CategoryResponse.class);
        CategoryResponse category = categoryResponse.getBody();
        Integer categoryId = Objects.requireNonNull(category).id();
        ResponseEntity<Void> response = restTemplate.exchange(
                CATEGORY_BASE_URL + "/" + categoryId,
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDelete_Failed() {
        ResponseEntity<Void> response = restTemplate.exchange(
                CATEGORY_BASE_URL + "/99999",
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}