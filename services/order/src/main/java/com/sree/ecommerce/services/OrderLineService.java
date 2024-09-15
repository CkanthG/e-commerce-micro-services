package com.sree.ecommerce.services;

import com.sree.ecommerce.mappers.OrderLineMapper;
import com.sree.ecommerce.models.OrderLineRequest;
import com.sree.ecommerce.models.OrderLineResponse;
import com.sree.ecommerce.repositories.OrderLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderLineService {

    private final OrderLineRepository repository;
    private final OrderLineMapper mapper;

    public Integer saveOrder(OrderLineRequest request) {
        return repository.save(mapper.toOrderLine(request)).getId();
    }

    public List<OrderLineResponse> findAllOrderLinesByOrderId(Integer orderId) {
        return repository.findAllByOrderId(orderId)
                .stream()
                .map(mapper::toOrderLineResponse)
                .toList();
    }
}
