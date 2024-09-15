package com.sree.ecommerce.services;

import com.sree.ecommerce.customer.Address;
import com.sree.ecommerce.customer.Customer;
import com.sree.ecommerce.exceptions.CustomerNotFoundException;
import com.sree.ecommerce.mappers.CustomerMapper;
import com.sree.ecommerce.models.CustomerRequest;
import com.sree.ecommerce.models.CustomerResponse;
import com.sree.ecommerce.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @InjectMocks
    CustomerService service;
    @Mock
    CustomerRepository repository;
    @Mock
    CustomerMapper mapper;
    private CustomerRequest customerRequest, customerUpdateRequest;
    private CustomerResponse customerResponse;
    private Customer customer;
    private static final String CUSTOMER_ID = UUID.randomUUID().toString();
    public static final String FIRSTNAME = "sree";
    public static final String LASTNAME = "kanth";
    public static final String EMAIL = "s@gmail.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Address address = new Address("abc", "85", "12099");
        customerRequest = new CustomerRequest(null, FIRSTNAME, LASTNAME, EMAIL, address);
        customerUpdateRequest = new CustomerRequest(CUSTOMER_ID, LASTNAME, FIRSTNAME, EMAIL, address);
        customer = new Customer(CUSTOMER_ID, FIRSTNAME, LASTNAME, EMAIL, address);
        customerResponse = new CustomerResponse(CUSTOMER_ID, FIRSTNAME, LASTNAME, EMAIL, address);
    }

    @Test
    void createCustomer_Success() {
        // when
        when(mapper.toCustomer(customerRequest)).thenReturn(customer);
        when(repository.save(customer)).thenReturn(customer);
        // then
        var actual = service.createCustomer(customerRequest);
        assertEquals("Created Customer ID : " + CUSTOMER_ID, actual);
        // verify
        verify(mapper, times(1)).toCustomer(customerRequest);
        verify(repository, times(1)).save(customer);
    }

    @Test
    void updateCustomer_Success() {
        // when
        when(repository.findById(CUSTOMER_ID)).thenReturn(Optional.ofNullable(customer));
        when(mapper.toCustomer(customerUpdateRequest)).thenReturn(customer);
        when(repository.save(customer)).thenReturn(customer);
        // then
        var actual = service.updateCustomer(customerUpdateRequest);
        assertEquals("Updated Customer ID : " + CUSTOMER_ID, actual);
        // verify
        verify(repository, times(1)).findById(CUSTOMER_ID);
        verify(mapper, times(1)).toCustomer(customerUpdateRequest);
        verify(repository, times(1)).save(customer);
    }

    @Test
    void updateCustomer_ThrowsCustomerNotFoundException() {
        assertThrows(
                CustomerNotFoundException.class,
                () -> service.updateCustomer(customerRequest)
        );
    }

    @Test
    void findAllCustomers_Success() {
        // when
        when(mapper.toCustomerResponse(customer)).thenReturn(customerResponse);
        when(repository.findAll()).thenReturn(List.of(customer));
        // then
        var actual = service.findAllCustomers();
        assertEquals(1, actual.size());
        // verify
        verify(mapper, times(1)).toCustomerResponse(customer);
        verify(repository, times(1)).findAll();
    }

    @Test
    void findCustomerById_Success() {
        // when
        when(mapper.toCustomerResponse(customer)).thenReturn(customerResponse);
        when(repository.findById(CUSTOMER_ID)).thenReturn(Optional.ofNullable(customer));
        // then
        var actual = service.findCustomerById(CUSTOMER_ID);
        assertEquals(customerResponse.firstname(), actual.firstname());
        assertEquals(customerResponse.lastname(), actual.lastname());
        assertEquals(customerResponse.email(), actual.email());
        // verify
        verify(mapper, times(1)).toCustomerResponse(customer);
        verify(repository, times(1)).findById(CUSTOMER_ID);
    }

    @Test
    void findCustomerById_ThrowsCustomerNotFoundException() {
        assertThrows(
                CustomerNotFoundException.class,
                () -> service.findCustomerById(UUID.randomUUID().toString())
        );
    }

    @Test
    void deleteCustomerById_Success() {
        // when
        when(repository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        // then
        service.deleteCustomerById(CUSTOMER_ID);
        // verify
        verify(repository, times(1)).findById(CUSTOMER_ID);
        verify(repository, times(1)).deleteById(CUSTOMER_ID);
    }

    @Test
    void deleteCustomerById_ThrowsCustomerNotFoundException() {
        assertThrows(
                CustomerNotFoundException.class,
                () -> service.deleteCustomerById(UUID.randomUUID().toString())
        );
    }
}