package com.sree.ecommerce.mappers;

import com.sree.ecommerce.entities.Order;
import com.sree.ecommerce.entities.OrderLine;
import com.sree.ecommerce.models.OrderLineRequest;
import com.sree.ecommerce.models.OrderLineResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderLineMapper {

    public OrderLine toOrderLine(OrderLineRequest request) {
        return OrderLine.builder()
                .id(request.id())
                .quantity(request.quantity())
                .productId(request.productId())
                .order(
                        Order.builder()
                                .id(request.orderId())
                                .build()
                )
                .build();
    }

    public OrderLineResponse toOrderLineResponse(OrderLine orderLine) {
        return new OrderLineResponse(
                orderLine.getId(),
                orderLine.getQuantity()
        );
    }
}
