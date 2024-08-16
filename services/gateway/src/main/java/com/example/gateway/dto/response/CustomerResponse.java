package com.example.gateway.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerResponse {
    String id;
    String firstname;
    String lastname;
    String username;
    String email;
    LocalDate dob;
    String address;
    String password;
    Set<String> roles;
}
