package com.ecommercialapp.product.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    CATEGORY_EXISTED(1001,"Category Existed"),
    CUSTOMER_NOT_EXISTED(1004,"Customer Not Existed"),
    UNCATEGORIZED_EXCEPTION(9999,"Uncategorized Exception"),
    USERNAME_INVALID(1002,"Username must be at least 3 characters"),
    PASSWORD_INVALID(1003,"Password must be at least 6 characters"),
    MongoSecurityException(1005,"Exception Authenticating"),
    QUANTITY_NOT_ENOUGH(1006,"Insufficient stock quantity for product with ID")
    ;

    int code;
    String message;
}
