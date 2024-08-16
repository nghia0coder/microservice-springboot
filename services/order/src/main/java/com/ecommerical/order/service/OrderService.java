package com.ecommerical.order.service;

import com.ecommerical.order.client.CustomerClient;
import com.ecommerical.order.client.PaymentClient;
import com.ecommerical.order.client.ProductClient;
import com.ecommerical.order.dto.request.OrderLineRequest;
import com.ecommerical.order.dto.request.OrderRequest;
import com.ecommerical.order.dto.request.PaymentRequest;
import com.ecommerical.order.dto.request.PurchaseRequest;
import com.ecommerical.order.dto.response.OrderResponse;
import com.ecommerical.order.entity.OrderLine;
import com.ecommerical.order.exception.AppException;
import com.ecommerical.order.exception.ErrorCode;
import com.ecommerical.order.kafka.OrderConfirmation;
import com.ecommerical.order.kafka.OrderProducer;
import com.ecommerical.order.mapper.OrderMapper;
import com.ecommerical.order.repositories.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class OrderService {


    OrderRepository orderRepository;
    CustomerClient customerClient;
    ProductClient productClient;
    OrderMapper orderMapper;
    OrderLineService orderLineService;
    OrderProducer orderProducer;
    PaymentClient paymentClient;



    @Transactional
    public OrderResponse CreatedOrder(OrderRequest request)
    {
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_EXISTED));

        var purchasedProducts =  this.productClient.purchaseProducts(request.products());

        var order = this.orderRepository.save(orderMapper.toOrder(request));

        List<OrderLine> orderLines = new ArrayList<>();
        for (PurchaseRequest purchaseRequest : request.products()) {
            var orderLineRequest = new OrderLineRequest(
                    order.getId(),
                    purchaseRequest.productId(),
                    purchaseRequest.quantity()
            );
            var orderLine = orderLineService.saveOrderLine(orderLineRequest, order);
            orderLines.add(orderLine);
        }


        order.setOrderLines(orderLines);


        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);

        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );


        return orderMapper.fromOrder(order);
    }

    public List<OrderResponse> findAllOrders() {
        return this.orderRepository.findAll()
                .stream()
                .map(this.orderMapper::fromOrder)
                .collect(Collectors.toList());
    }
    public OrderResponse findById(Integer id) {
        return this.orderRepository.findById(id)
                .map(this.orderMapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No order found with the provided ID: %d", id)));
    }

    public List<OrderResponse> findOrdersByCustomerId(String customerId) {
        var orders = this.orderRepository.findByCustomerId(customerId);
        return orders.stream()
                .map(orderMapper::fromOrder)
                .collect(Collectors.toList());
    }
}
