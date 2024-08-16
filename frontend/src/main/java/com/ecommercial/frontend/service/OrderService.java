package com.ecommercial.frontend.service;


import com.ecommercial.frontend.models.*;
import com.ecommercial.frontend.models.request.OrderRequestDTO;
import com.ecommercial.frontend.models.request.PurchaseRequest;
import com.ecommercial.frontend.models.request.RequestDTO;
import com.ecommercial.frontend.models.response.APIResponse;
import com.ecommercial.frontend.ultilities.SD;
import com.ecommercial.frontend.ultilities.UrlConfig;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class OrderService {
    BaseService baseService;

    @Autowired
    UrlConfig applicationConfig;

    public APIResponse placeOrder(HttpSession session, PaymentMethod paymentMethod)
    {
        String customerId =(String) session.getAttribute("customerId");
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        List<PurchaseRequest> purchaseRequests = cart.stream()
                .map(cartItem -> new PurchaseRequest(cartItem.getProductId(), cartItem.getQuantity()))
                .collect(Collectors.toList());

        BigDecimal total = cart.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrderRequestDTO orderRequestDTO = new OrderRequestDTO(null,total, paymentMethod,customerId,purchaseRequests);

        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setAPItype(SD.ApiType.POST);
        requestDTO.setUrl(applicationConfig.getOrderUrl());
        requestDTO.setData(orderRequestDTO);
        requestDTO.setContentType(SD.ContentType.Json);

        return baseService.Send(requestDTO);
    }
}
