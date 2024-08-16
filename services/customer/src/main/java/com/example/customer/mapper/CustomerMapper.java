package com.example.customer.mapper;

import com.example.customer.dto.request.CustomerRequest;
import com.example.customer.dto.request.CustomerUpdateRequest;
import com.example.customer.dto.response.CustomerResponse;
import com.example.customer.entity.Customer;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toCustomer(CustomerRequest request) {
        if (request == null) {
            return null;
        }
        return Customer.builder()
                .id(request.getId())
                .username(request.getUsername())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .address(request.getAddress())
                .dob(request.getDob())
                .password(request.getPassword())
                .build();
    }

    public CustomerResponse fromCustomer(Customer customer) {
        if (customer == null) {
            return null;
        }
        return CustomerResponse.builder()
                .id(customer.getId())
                .username(customer.getUsername())
                .firstname(customer.getFirstname())
                .lastname(customer.getLastname())
                .email(customer.getEmail())
                .address(customer.getAddress())
                .dob(customer.getDob())
                .password(customer.getPassword())
                .roles(customer.getRoles())
                .build();
    }
    public void updateCustomer(Customer customer, CustomerUpdateRequest request) {
        if (request.getUserName() != null) {
            customer.setUsername(request.getUserName());
        }
        if (request.getFirstname() != null) {
            customer.setFirstname(request.getFirstname());
        }
        if (request.getLastname() != null) {
            customer.setLastname(request.getLastname());
        }
        if (request.getEmail() != null) {
            customer.setEmail(request.getEmail());
        }
        if (request.getDob() != null) {
            customer.setDob(request.getDob());
        }
        if (request.getAddress() != null) {
            customer.setAddress(request.getAddress());
        }
    }

}
