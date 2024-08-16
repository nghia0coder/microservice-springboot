package com.ecommercial.frontend.models;

import com.ecommercial.frontend.models.request.OrderRequestDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StripeRequestDTO {
    String StripeSessionUrl;
    String StripeSessionId;
    String ApproveUrl;
    String CancelUrl;
    OrderRequestDTO orderRequestDTO;
}
