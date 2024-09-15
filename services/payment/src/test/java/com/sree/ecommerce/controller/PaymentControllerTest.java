package com.sree.ecommerce.controller;

import com.sree.ecommerce.models.Customer;
import com.sree.ecommerce.models.PaymentMethod;
import com.sree.ecommerce.models.PaymentRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class PaymentControllerTest {

    @LocalServerPort
    private int port;
    private String paymentUrl;
    @Autowired
    private TestRestTemplate testRestTemplate;

    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
            .withUsername("postgres")
            .withPassword("password")
            .withDatabaseName("test");

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"))
            .withEmbeddedZookeeper();

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> postgreSQLContainer.getJdbcUrl());
        registry.add("spring.datasource.username", () -> postgreSQLContainer.getUsername());
        registry.add("spring.datasource.password", () -> postgreSQLContainer.getPassword());
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        // Kafka properties
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
        kafka.start();
        System.setProperty("POSTGRES_PORT", postgreSQLContainer.getMappedPort(5432).toString());
        System.setProperty("KAFKA_SERVER", kafka.getBootstrapServers());
        System.setProperty("KAFKA_ADVERTISED_LISTENERS", kafka.getBootstrapServers());
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
        kafka.stop();
    }

    @BeforeEach
    void setUp() {
        paymentUrl = "http://localhost:" + port + "/api/v1/payments";
    }

    @Test
    void testPayment_Success() {
        PaymentRequest paymentRequest = new PaymentRequest(
                1,
                BigDecimal.valueOf(200.0),
                PaymentMethod.PAYPAL,
                1,
                "HGDHCHGCF",
                new Customer(
                        UUID.randomUUID().toString(),
                        "sree",
                        "kanth",
                        "s@gmail.com"
                ));
        ResponseEntity<Integer> responseEntity = testRestTemplate.postForEntity(
                paymentUrl,
                paymentRequest,
                Integer.class
        );
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}