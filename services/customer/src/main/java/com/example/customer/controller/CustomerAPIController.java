package com.example.customer.controller;


import com.example.customer.dto.request.CustomerRequest;

import com.example.customer.dto.request.CustomerUpdateRequest;
import com.example.customer.dto.response.APIResponse;
import com.example.customer.dto.response.CustomerResponse;
import com.example.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api/customer")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RestController
public class CustomerAPIController {

    @Autowired
    CustomerService customerService;

    @PostMapping
    public APIResponse<CustomerResponse> createCustomer(
            @RequestBody @Valid CustomerRequest request
    ) {
        return APIResponse.<CustomerResponse>builder()
                .result(customerService.createCustomer(request))
                .build();
    }

    @GetMapping("/{customer-id}")
    public ResponseEntity<CustomerResponse> findById(
            @PathVariable("customer-id") String customerId
    ) {
        return ResponseEntity.ok(this.customerService.getCustomerById(customerId));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<CustomerResponse> findByUsername(
            @PathVariable("username") String username
    ) {
        return ResponseEntity.ok(this.customerService.getCustomerByUsername(username));
    }


    @GetMapping
    APIResponse<List<CustomerResponse>> getAllCustomers()
    {
        return APIResponse.<List<CustomerResponse>>builder()
                .result(customerService.findAllCustomers())
                .build();
    }

    @DeleteMapping("/{customerId}")
    APIResponse<String> deleteCustomer(@PathVariable String customerId)
    {
        customerService.deleteCustomer(customerId);
        return  APIResponse.<String>builder()
                .result("User Has Been Deleted")
                .build();
    }
    @PutMapping("/{customerId}")
    APIResponse<CustomerResponse> updateCustomer(@PathVariable String customerId, @RequestBody CustomerUpdateRequest request)
    {
        return  APIResponse.<CustomerResponse>builder()
                .result(customerService.updateCustomer(customerId,request))
                .build();
    }


}
