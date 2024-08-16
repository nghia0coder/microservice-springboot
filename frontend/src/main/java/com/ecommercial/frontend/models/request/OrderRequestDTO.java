package com.ecommercial.frontend.models.request;



import com.ecommercial.frontend.models.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequestDTO(
        Integer id,
        BigDecimal amount,

        PaymentMethod paymentMethod,

        String customerId,

        List<PurchaseRequest> products
        ) {

}
