package com.example.customer.configuration;

import com.example.customer.entity.Customer;
import com.example.customer.enums.Role;
import com.example.customer.repositories.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Slf4j
@Configuration
public class ApplicationConfig {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(CustomerRepository customerRepository)
    {
        return args -> {
            if(customerRepository.findByUsername("admin").isEmpty())
            {
                var roles = new HashSet<String>();
                roles.add(Role.ADMIN.name());

                Customer customer = Customer.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();
                customerRepository.save(customer);
                log.warn("admin user has been created with default");
            }
        };
    }
}
