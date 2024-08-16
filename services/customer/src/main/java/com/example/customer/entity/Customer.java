package com.example.customer.entity;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer {
     @Id
     String id;
     String firstname;
     String lastname;
     String username;
     @Indexed(unique = true)
     String email;
     LocalDate dob;
     String address;
     String password;

     Set<String> roles;
}
