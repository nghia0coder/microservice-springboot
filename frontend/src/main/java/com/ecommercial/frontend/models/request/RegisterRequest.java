package com.ecommercial.frontend.models.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    String id;
    @Size(min = 3 , message = "User name must at least 6 characters")
    String username;
    String firstname;
    String lastname;
    String email;
    String address;
    LocalDate dob;
    @Size(min = 6 , message = "Password must at least 3 characters")
    String password;
}

