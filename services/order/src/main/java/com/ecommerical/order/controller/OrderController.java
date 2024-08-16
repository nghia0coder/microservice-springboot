package com.ecommerical.order.controller;

import com.ecommerical.order.dto.request.OrderRequest;
import com.ecommerical.order.dto.response.APIResponse;
import com.ecommerical.order.dto.response.OrderResponse;
import com.ecommerical.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class OrderController {
    private final OrderService service;

    @Value("${stripe.secretKey}")
    private String key;
    @PostMapping
    public APIResponse<OrderResponse> createOrder(
            @RequestBody @Valid OrderRequest request
    ) {
        return APIResponse.<OrderResponse>builder()
                .result(service.CreatedOrder(request))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAll() {
        return ResponseEntity.ok(this.service.findAllOrders());
    } 

    @GetMapping("/{order-id}")
    public ResponseEntity<OrderResponse> findById(
            @PathVariable("order-id") Integer orderId
    ) {
        return ResponseEntity.ok(this.service.findById(orderId));
    }

    @GetMapping("/customer/{customer-id}")
    public List<OrderResponse> getOrdersByCustomerId(@PathVariable("customer-id") String customerId) {
        return service.findOrdersByCustomerId(customerId);
    }


}
