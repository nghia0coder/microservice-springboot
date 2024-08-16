package com.example.customer.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerUpdateRequest {
    String firstname;
    String lastname;
    String userName;
    String email;
    String address;
    LocalDate dob;
    @Size(min = 6 , message = "PASSWORD_INVALID")
    String password;
}
