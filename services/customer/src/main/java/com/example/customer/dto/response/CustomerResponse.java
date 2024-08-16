package com.example.customer.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class CustomerResponse {
    String id;
    String username;
    String firstname;
    String lastname;
    String email;
    String address;
    LocalDate dob;
    String password;
    Set<String> roles;
}
