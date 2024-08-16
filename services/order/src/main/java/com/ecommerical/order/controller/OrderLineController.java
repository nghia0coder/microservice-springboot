package com.ecommerical.order.controller;

import com.ecommerical.order.dto.response.OrderLineResponse;
import com.ecommerical.order.mapper.OrderLineMapper;
import com.ecommerical.order.service.OrderLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order-lines")
@RequiredArgsConstructor
public class OrderLineController {

    private final OrderLineService service;
    private final OrderLineMapper mapper;

    @GetMapping("/order/{order-id}")
    public ResponseEntity<List<OrderLineResponse>> findByOrderId(
            @PathVariable("order-id") Integer orderId
    ) {
        return ResponseEntity.ok(service.findAllByOrderId(orderId));
    }
}

