package com.sree.ecommerce.controllers;

import com.sree.ecommerce.models.OrderRequest;
import com.sree.ecommerce.models.OrderResponse;
import com.sree.ecommerce.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createOrder(request));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAll() {
        return ResponseEntity.ok(service.findAllOrders());
    }

    @GetMapping("/{order-id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable("order-id") Integer orderId) {
        return ResponseEntity.ok(service.findOrderById(orderId));
    }
}
