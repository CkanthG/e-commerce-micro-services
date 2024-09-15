package com.sree.ecommerce.mappers;

import com.sree.ecommerce.entities.Order;
import com.sree.ecommerce.models.OrderRequest;
import com.sree.ecommerce.models.OrderResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public Order toOrder(OrderRequest request) {
        return Order.builder()
                .id(request.id())
                .reference(request.reference())
                .paymentMethod(request.paymentMethod())
                .totalAmount(request.amount())
                .customerId(request.customerId())
                .build();
    }

    public OrderResponse toOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getReference(),
                order.getTotalAmount(),
                order.getPaymentMethod(),
                order.getCustomerId()
        );
    }
}
