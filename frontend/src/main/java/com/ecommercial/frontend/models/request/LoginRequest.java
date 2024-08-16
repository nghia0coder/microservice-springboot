package com.ecommercial.frontend.models.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class LoginRequest {
    String username;
    String password;
}
