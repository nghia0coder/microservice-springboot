package com.example.customer.service;

import com.example.customer.dto.request.CustomerRequest;
import com.example.customer.dto.request.CustomerUpdateRequest;
import com.example.customer.dto.response.CustomerResponse;
import com.example.customer.entity.Customer;
import com.example.customer.enums.Role;
import com.example.customer.exception.AppException;
import com.example.customer.exception.ErrorCode;
import com.example.customer.mapper.CustomerMapper;
import com.example.customer.repositories.CustomerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CustomerService {

    CustomerRepository customerRepository;
    CustomerMapper customerMapper;
    PasswordEncoder passwordEncoder;
    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);


    public List<CustomerResponse> findAllCustomers() {
        try {
            return this.customerRepository.findAll()
                    .stream()
                    .map(this.customerMapper::fromCustomer)
                    .collect(Collectors.toList());
        } catch (Exception e) {
           throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
    public CustomerResponse createCustomer(CustomerRequest request) {
        var customer = customerMapper.toCustomer(request);
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.CUSTOMER.name());

        try {
            customer.setRoles(roles);
            customer = customerRepository.save(customer);

        }
        catch (DuplicateKeyException exception)
        {
            throw new AppException(ErrorCode.CUSTOMER_EXISTED);
        }
        return customerMapper.fromCustomer(customer);
    }


    public CustomerResponse getCustomerById(String customerId)
    {
        return customerMapper.fromCustomer(
                customerRepository.findById(customerId).orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_EXISTED))
        );
    }

    public CustomerResponse getCustomerByUsername(String username)
    {
        return customerMapper.fromCustomer(
                customerRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_EXISTED))
        );
    }

    public void deleteCustomer(String customerId)
    {
        customerRepository.deleteById(customerId);
    }

    public CustomerResponse updateCustomer(String customerId, CustomerUpdateRequest request)
    {
        Customer customer = customerRepository.findById(customerId).orElseThrow(
                () -> new AppException(ErrorCode.CUSTOMER_NOT_EXISTED)
        );
        customerMapper.updateCustomer(customer, request);

        try {
            customerRepository.save(customer);
        }
        catch (DuplicateKeyException e)
        {
            throw  new AppException(ErrorCode.CUSTOMER_EXISTED);
        }
        customer.setPassword(passwordEncoder.encode(request.getPassword()));

        return customerMapper.fromCustomer(customer);

    }

}
