package com.ecommerical.order.service;

import com.ecommerical.order.dto.request.OrderLineRequest;
import com.ecommerical.order.dto.response.OrderLineResponse;
import com.ecommerical.order.entity.Order;
import com.ecommerical.order.entity.OrderLine;
import com.ecommerical.order.mapper.OrderLineMapper;
import com.ecommerical.order.repositories.OrderLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderLineService {
    @Autowired
    private final OrderLineRepository repository;
    @Autowired
    private final OrderLineMapper mapper;

    public OrderLine saveOrderLine(OrderLineRequest request, Order order) {
        var orderLine = mapper.toOrderLine(request,order);
        repository.save(orderLine);
        return orderLine;
    }

    public List<OrderLineResponse> findAllByOrderId(Integer orderId) {
        return repository.findAllByOrderId(orderId)
                .stream()
                .map(mapper::toOrderLineResponse)
                .collect(Collectors.toList());
    }


}
