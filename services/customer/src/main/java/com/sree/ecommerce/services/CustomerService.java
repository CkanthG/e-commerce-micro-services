package com.sree.ecommerce.services;

import com.sree.ecommerce.exceptions.CustomerNotFoundException;
import com.sree.ecommerce.mappers.CustomerMapper;
import com.sree.ecommerce.models.CustomerRequest;
import com.sree.ecommerce.models.CustomerResponse;
import com.sree.ecommerce.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;

    public String createCustomer(CustomerRequest request) {
        var customer = customerRepository.save(mapper.toCustomer(request));
        return format("Created Customer ID : %s", customer.getId());
    }

    public String updateCustomer(CustomerRequest request) {
        customerRepository.findById(request.id())
                .orElseThrow(() -> new CustomerNotFoundException(
                        format("Customer update failed, No Customer found with provided ID: %s", request.id()))
                );
        var customer = customerRepository.save(mapper.toCustomer(request));
        return format("Updated Customer ID : %s", customer.getId());
    }

    public List<CustomerResponse> findAllCustomers() {
        return customerRepository.findAll().stream().map(mapper::toCustomerResponse).toList();
    }

    public CustomerResponse findCustomerById(String customerId) {
        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(
                        format("No Customer found with provided ID: %s", customerId)
                )
        );
        return mapper.toCustomerResponse(customer);
    }

    public void deleteCustomerById(String customerId) {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(
                                format("No Customer found with provided ID: %s", customerId)
                        )
                );
        customerRepository.deleteById(customerId);
    }
}
