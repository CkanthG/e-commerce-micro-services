package com.sree.ecommerce.mappers;

import com.sree.ecommerce.customer.Customer;
import com.sree.ecommerce.models.CustomerRequest;
import com.sree.ecommerce.models.CustomerResponse;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {

    public Customer toCustomer(CustomerRequest customerRequest) {
        if (customerRequest == null) {
            return null;
        }
        return Customer.builder()
                .email(customerRequest.email())
                .firstname(customerRequest.firstname())
                .lastname(customerRequest.lastname())
                .address(customerRequest.address())
                .build();
    }

    public CustomerResponse toCustomerResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getAddress()
        );
    }
}
