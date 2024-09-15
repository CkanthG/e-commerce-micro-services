package com.sree.ecommerce.controllers;

import com.sree.ecommerce.models.OrderLineResponse;
import com.sree.ecommerce.services.OrderLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-lines")
@RequiredArgsConstructor
public class OrderLineController {

    private final OrderLineService orderLineService;

    @GetMapping("/order/{order-id}")
    public ResponseEntity<List<OrderLineResponse>> getOrderLineById(@PathVariable("order-id") Integer orderId) {
        return ResponseEntity.ok(orderLineService.findAllOrderLinesByOrderId(orderId));
    }
}
