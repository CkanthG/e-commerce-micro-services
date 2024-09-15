package com.sree.ecommerce.controllers;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

public abstract class AbstractTestContainers {

    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
            .withUsername("postgres")
            .withPassword("password")
            .withDatabaseName("test");
    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"))
            .withEmbeddedZookeeper();

    static {
        postgreSQLContainer.start();
        kafka.start();
        // Optionally, if you want the container to be reused across tests (faster initialization)
        postgreSQLContainer.withReuse(true);
        kafka.withReuse(true);
    }

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> postgreSQLContainer.getJdbcUrl());
        registry.add("spring.datasource.username", () -> postgreSQLContainer.getUsername());
        registry.add("spring.datasource.password", () -> postgreSQLContainer.getPassword());
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        // Kafka properties
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

}
