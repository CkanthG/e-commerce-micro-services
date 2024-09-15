package com.sree.ecommerce.services;

import com.sree.ecommerce.models.PurchaseResponse;
import com.sree.ecommerce.models.PurchaseRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@FeignClient(
        name = "product-service",
        url = "${application.config.product-url}"
)
public interface ProductFeignClient {

    @PostMapping("/purchase")
    Optional<List<PurchaseResponse>> purchaseProduct(@RequestBody List<PurchaseRequest> requests);

}
