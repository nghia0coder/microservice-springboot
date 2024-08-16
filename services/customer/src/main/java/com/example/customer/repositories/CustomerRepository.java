package com.example.customer.repositories;

import com.example.customer.dto.response.CustomerResponse;
import com.example.customer.entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends MongoRepository<Customer,String> {
    Optional<Customer> findByUsername(String username);
}