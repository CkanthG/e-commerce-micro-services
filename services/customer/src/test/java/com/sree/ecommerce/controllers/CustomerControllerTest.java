package com.sree.ecommerce.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sree.ecommerce.customer.Address;
import com.sree.ecommerce.models.CustomerRequest;
import com.sree.ecommerce.models.CustomerResponse;
import com.sree.ecommerce.services.CustomerService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CustomerControllerTest{

    public static String CUSTOMER_BASE_URL;

    @Autowired
    private CustomerService service;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @LocalServerPort
    private int port;
    private Address address;
    private CustomerRequest customerRequest;

    // Define the MongoDB container with a specific image version
    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    // Static block to start the container once for all test cases
    @BeforeAll
    public static void startContainer() {
        mongoDBContainer.start();  // Start the MongoDB container before running any test
    }

    // Dynamically register MongoDB properties (url, username, password, etc.) for Spring
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @AfterAll
    public static void stopContainer() {
        mongoDBContainer.stop();  // Stop the container after all tests are done
    }

    @BeforeEach
    void setUp() {
        CUSTOMER_BASE_URL = "http://localhost:" + port + "/api/v1/customers";
        address = new Address("abc", "85", "12099");
        customerRequest = new CustomerRequest(null, "sree", "kanth", "s@gmail.com", address);
    }

    @Test
    void testCreateCustomer_Success() {
        // save customer
        var response = restTemplate.postForEntity(CUSTOMER_BASE_URL, customerRequest, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testCreate_Failed() {
        var response = restTemplate.postForEntity(CUSTOMER_BASE_URL, null, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUpdate_Success() {
        // save customer
        var response = restTemplate.postForEntity(CUSTOMER_BASE_URL, customerRequest, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        String customerId = getCustomerIdFromStringResponse(response);
        customerRequest = new CustomerRequest(customerId, "kanth", "sree", "sree@gmail.com", address);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<CustomerRequest> entity = new HttpEntity<>(customerRequest, httpHeaders);
        // update customer
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                CUSTOMER_BASE_URL,
                HttpMethod.PUT,
                entity,
                String.class
        );
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testUpdate_Failed() {
        // save customer
        var response = restTemplate.postForEntity(CUSTOMER_BASE_URL, customerRequest, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                CUSTOMER_BASE_URL,
                HttpMethod.PUT,
                null,
                String.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testFindAll_Success() {
        // save customer
        restTemplate.postForEntity(CUSTOMER_BASE_URL, customerRequest, Void.class);
        // get all customers
        ResponseEntity<CustomerResponse[]> response = restTemplate.getForEntity(CUSTOMER_BASE_URL, CustomerResponse[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("sree", Objects.requireNonNull(response.getBody())[0].firstname());
        assertEquals("kanth", response.getBody()[0].lastname());
        assertEquals("s@gmail.com", response.getBody()[0].email());
    }

    @Test
    void testFindById_Success() {
        // save customer
        var response = restTemplate.postForEntity(CUSTOMER_BASE_URL, customerRequest, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        String customerId = getCustomerIdFromStringResponse(response);
        // get customer by id
        ResponseEntity<CustomerResponse> responseEntity = restTemplate.getForEntity(CUSTOMER_BASE_URL + "/" + customerId, CustomerResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(customerRequest.firstname(), Objects.requireNonNull(responseEntity.getBody()).firstname());
        assertEquals(customerRequest.lastname(), responseEntity.getBody().lastname());
        assertEquals(customerRequest.email(), responseEntity.getBody().email());
    }

    @Test
    void testFindById_Failed() {

        ResponseEntity<CustomerResponse> responseEntity = restTemplate.getForEntity(
                CUSTOMER_BASE_URL + "/" + UUID.randomUUID(),
                CustomerResponse.class
        );
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testDelete_Success() {
        // save customer
        var response = restTemplate.postForEntity(CUSTOMER_BASE_URL, customerRequest, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        String customerId = getCustomerIdFromStringResponse(response);
        // delete customer
        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                CUSTOMER_BASE_URL + "/" + customerId,
                HttpMethod.DELETE,
                null,
                Void.class);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    void testDelete_Failed() {
        // delete customer
        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                CUSTOMER_BASE_URL + "/" + UUID.randomUUID(),
                HttpMethod.DELETE,
                null,
                Void.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    private static @NotNull String getCustomerIdFromStringResponse(ResponseEntity<String> response) {
        String[] splitData = Objects.requireNonNull(response.getBody()).split(":");
        return splitData[1].trim();
    }
}