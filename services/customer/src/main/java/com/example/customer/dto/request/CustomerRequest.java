package com.example.customer.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerRequest {
    String id;
    @Size(min = 3 , message = "USERNAME_INVALID")
     String username;
     String firstname;
     String lastname;
     String email;
     String address;
     LocalDate dob;
    @Size(min = 6 , message = "PASSWORD_INVALID")
     String password;
}
