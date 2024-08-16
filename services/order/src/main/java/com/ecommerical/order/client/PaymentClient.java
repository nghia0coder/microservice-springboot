package com.ecommerical.order.client;

import com.ecommerical.order.dto.request.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "product-service",
        url = "${application.config.payment-url}"
)
@Service
public interface PaymentClient {
    @PostMapping
    Integer requestOrderPayment(@RequestBody PaymentRequest request);
}
