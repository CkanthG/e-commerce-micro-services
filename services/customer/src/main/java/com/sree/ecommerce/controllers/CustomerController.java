package com.sree.ecommerce.controllers;

import com.sree.ecommerce.models.CustomerRequest;
import com.sree.ecommerce.models.CustomerResponse;
import com.sree.ecommerce.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid CustomerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(request));
    }

    @PutMapping
    public ResponseEntity<String> update(
            @RequestBody @Valid CustomerRequest request
    ) {
        return ResponseEntity.ok(customerService.updateCustomer(request));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAll() {
        return ResponseEntity.ok(customerService.findAllCustomers());
    }

    @GetMapping("/{customer-id}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable(name = "customer-id") String customerId) {
        return ResponseEntity.ok(customerService.findCustomerById(customerId));
    }

    @DeleteMapping("/{customer-id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "customer-id") String customerId) {
        customerService.deleteCustomerById(customerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
