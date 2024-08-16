package com.example.gateway.client;

import com.example.gateway.configuration.FeignConfig;
import com.example.gateway.dto.response.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(
        name = "customer-service",
        url = "${application.config.customer-url}",
        configuration = FeignConfig.class // Ensure this line is included
)
public interface CustomerClient {
    @GetMapping("/username/{username}")
    Optional<CustomerResponse> findCustomerByUserName(@PathVariable("username") String username);
}