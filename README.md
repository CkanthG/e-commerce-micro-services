# E-Commerce SpringBoot Micro Services

---
### **Below Tech Stacks Used**
```
Java 21
SpringBoot 3.3.2
PostgreSQL
MongoDB
Keycloak
Zookeeper
Kafka
Zipkin
MailDev
CircuitBreaker
Flyway
```

### **Microservices Overview**

This project is built using the **Microservices architecture**, where each service is designed to handle a specific domain of the business. The services communicate with each other using REST APIs and Kafka for asynchronous messaging. Below are the individual services:

---

### **1. Config Server**
The **Config Server** is a centralized configuration service for all microservices. It stores and serves configuration properties for each service, ensuring consistency and flexibility. The configurations are usually stored in a remote Git repository, making it easier to manage and update settings across all microservices in the ecosystem.

- **Purpose**: Centralized configuration management.
- **Responsibilities**:
    - Externalized configurations for microservices.
    - Provides dynamic configuration updates without redeploying services.
    - Secure storage for sensitive data like credentials (can be encrypted in realtime).

---

### **2. Customer Service**
The **Customer Service** manages customer-related operations such as creating new customers, retrieving customer details, and updating customer information. It serves as the main service for handling customer data and is typically integrated with other services like Order and Payment services.

- **Purpose**: Manage customer data and provide customer-related operations.
- **Responsibilities**:
    - Customer registration and authentication.
    - CRUD operations on customer information.
    - Integration with Order and Payment services.

---

### **3. Discovery Server**
The **Discovery Server** (implemented using **Eureka**) is responsible for maintaining a registry of all available microservices. It enables service discovery, where microservices can register themselves and discover other services dynamically, facilitating load balancing and fault tolerance.

- **Purpose**: Service discovery and dynamic load balancing.
- **Responsibilities**:
    - Keep track of available instances of microservices.
    - Allow services to find each other dynamically.
    - Ensure high availability through service registration and deregistration.

---

### **4. Gateway Service**
The **Gateway Service** acts as a single entry point for clients (e.g., web or mobile apps) to communicate with the underlying microservices. It routes requests to the appropriate microservice, handles authentication, logging, and security(Keycloak).

- **Purpose**: API gateway to manage and route client requests.
- **Responsibilities**:
    - Forward client requests to the appropriate microservices.
    - Handle authentication, authorization, and request filtering.
    - Perform load balancing.
    - Ensure security(Keycloak) by exposing only specific APIs.

---

### **5. Notification Service**
The **Notification Service** is responsible for sending notifications to customers, either through emails, SMS, or push notifications. It listens to relevant events (such as order placement or payment completion) via **Kafka**, which allows for efficient, real-time communication between microservices.

- **Purpose**: Send notifications based on specific events.
- **Responsibilities**:
    - Listen to events via Kafka and trigger notifications.
    - Support multiple notification channels like email(MailDev) notifications.
    - Ensure delivery of critical information (e.g., order confirmation or payment confirmation).

---

### **6. Order Service**
The **Order Service** handles all operations related to customer orders, such as placing new orders, updating order status, and retrieving order history. It communicates with the **Customer Service**, **Product Service**, and **Payment Service** to process orders end-to-end.

- **Purpose**: Manage customer orders and order processing workflows.
- **Responsibilities**:
    - Create, update, and track orders.
    - Coordinate with the **Payment Service** to handle payments.
    - Communicate with the **Product Service** to manage inventory.
    - Send order events to the **Notification Service** for order confirmations.

---

### **7. Payment Service**
The **Payment Service** manages the processing of customer payments, interacting with third-party payment gateways to handle transactions securely. It ensures the success or failure of payments and informs the **Order Service** accordingly.

- **Purpose**: Handle and process payments.
- **Responsibilities**:
    - Process payments via third-party payment gateways.
    - Ensure secure transactions and store payment records.
    - Communicate with the **Order Service** to update the order status based on payment success or failure.

---

### **8. Product Service**
The **Product Service** manages the product catalog, including product details, availability, and pricing. It provides product information to other services, such as the **Order Service** and the **Customer Service**, for order placement and customer interactions.

- **Purpose**: Manage product catalog and inventory.
- **Responsibilities**:
    - CRUD operations on products (create, update, delete).
    - Provide product details and availability to other services.
    - Integrate with the **Order Service** to manage product stock and inventory.

---

### **Conclusion**

Each service plays a distinct role in this microservices ecosystem, promoting loose coupling and scalability. The services communicate via REST APIs and Kafka (for asynchronous messaging), with **Config Server** managing configuration centrally, and **Discovery Server** enabling dynamic service discovery.

---

## **Micro Services - Execution Flow**
Start in a sequence like below to test in your local environments.
```
Config Server
Discovery Server
Customer Service
Product Service
Payment Service
Notification Service
Order Service
Gateway Service
```
