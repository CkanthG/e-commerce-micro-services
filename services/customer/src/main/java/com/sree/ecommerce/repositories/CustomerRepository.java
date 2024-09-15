package com.sree.ecommerce.repositories;

import com.sree.ecommerce.customer.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
}
