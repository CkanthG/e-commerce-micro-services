package com.sree.ecommerce.services;

import com.sree.ecommerce.exceptions.BusinessException;
import com.sree.ecommerce.exceptions.OrderNotFoundException;
import com.sree.ecommerce.kafka.OrderConfirmation;
import com.sree.ecommerce.kafka.OrderProducer;
import com.sree.ecommerce.mappers.OrderMapper;
import com.sree.ecommerce.models.*;
import com.sree.ecommerce.repositories.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final CustomerFeignClient customerFeignClient;
    private final ProductFeignClient productFeignClient;
    private final OrderLineService orderLineService;
    private final OrderProducer producer;
    private final PaymentFeignClient paymentFeignClient;
    private static final String ORDER_SERVICE = "order-service";

    @CircuitBreaker(name = ORDER_SERVICE, fallbackMethod = "orderFallbackMethod")
    public String createOrder(OrderRequest request) {
        var customer = customerFeignClient.findCustomerById(request.customerId()).orElseThrow(
                () -> new BusinessException(
                        format("Cannot create order, there is no customer exists with provided ID : %s", request.customerId())
                )
        );

        var purchasedProducts = productFeignClient.purchaseProduct(request.products()).orElseThrow(
                () -> new BusinessException("Error occurred while purchasing the products")
        );

        var order = repository.save(mapper.toOrder(request));

        for (PurchaseRequest purchaseRequest : request.products()) {
            orderLineService.saveOrder(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

        paymentFeignClient.requestOrderPayment(
                new PaymentRequest(
                        request.amount(),
                        request.paymentMethod(),
                        order.getId(),
                        order.getReference(),
                        customer
                )
        );

        producer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );
        return format("Successfully processed order with ID: %d", order.getId());
    }

    public String orderFallbackMethod(Exception e) {
        log.error("One of the Service is DOWN, please try again after sometime, Exception : {}", e.getLocalizedMessage());
        return "One of the Service is DOWN, please try again after sometime, Exception : " + e.getLocalizedMessage();
    }

    public List<OrderResponse> findAllOrders() {
        return repository.findAll()
                .stream()
                .map(mapper::toOrderResponse)
                .toList();
    }

    public OrderResponse findOrderById(Integer orderId) {
        return repository.findById(orderId)
                .map(mapper::toOrderResponse)
                .orElseThrow(
                () -> new OrderNotFoundException(format("No order found with specified ID : %d", orderId))
        );
    }


}
