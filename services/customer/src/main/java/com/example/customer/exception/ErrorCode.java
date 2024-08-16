package com.example.customer.exception;

import com.mongodb.MongoSecurityException;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    CUSTOMER_EXISTED(1001,"Customer Existed. Email Already Register"),
    CUSTOMER_NOT_EXISTED(1004,"Customer Not Existed"),
    UNCATEGORIZED_EXCEPTION(9999,"Uncategorized Exception"),
    USERNAME_INVALID(1002,"Username must be at least 3 characters"),
    PASSWORD_INVALID(1003,"Password must be at least 6 characters"),
    MongoSecurityException(1005,"Exception Authenticating")
    ;

    int code;
    String message;
}
