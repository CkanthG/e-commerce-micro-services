package com.sree.ecommerce.mappers;

import com.sree.ecommerce.customer.Address;
import com.sree.ecommerce.customer.Customer;
import com.sree.ecommerce.models.CustomerRequest;
import com.sree.ecommerce.models.CustomerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerMapperTest {

    @InjectMocks
    CustomerMapper customerMapper;

    Customer customer;
    CustomerRequest customerRequest;
    CustomerResponse customerResponse;
    private static final String CUSTOMER_ID = UUID.randomUUID().toString();
    public static final String FIRSTNAME = "sree";
    public static final String LASTNAME = "kanth";
    public static final String EMAIL = "s@gmail.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Address address = new Address("abc", "85", "12099");
        customerRequest = new CustomerRequest(null, FIRSTNAME, LASTNAME, EMAIL, address);
        customer = new Customer(CUSTOMER_ID, FIRSTNAME, LASTNAME, EMAIL, address);
        customerResponse = new CustomerResponse(CUSTOMER_ID, FIRSTNAME, LASTNAME, EMAIL, address);
    }

    @Test
    void toCustomer_Success() {
        // then
        var actual = customerMapper.toCustomer(customerRequest);
        assertEquals(customerRequest.firstname(), actual.getFirstname());
        assertEquals(customerRequest.lastname(), actual.getLastname());
        assertEquals(customerRequest.email(), actual.getEmail());
    }

    @Test
    void toCustomer_ReturnNull() {
        // then
        var actual = customerMapper.toCustomer(null);
        assertNull(actual);
    }

    @Test
    void toCustomerResponse_Success() {
        // then
        var actual = customerMapper.toCustomerResponse(customer);
        assertEquals(customer.getFirstname(), actual.firstname());
        assertEquals(customer.getLastname(), actual.lastname());
        assertEquals(customer.getEmail(), actual.email());
    }
}