package com.ecommerical.order.exception;


import com.ecommerical.order.dto.response.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<APIResponse> handlingAppException(AppException exception)
    {
        ErrorCode errorCode = exception.getErrorCode();

        APIResponse apiResponse = new APIResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<APIResponse> handlingRuntimeException(RuntimeException exception)
    {


        APIResponse apiResponse = new APIResponse();

        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(exception.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }
    @ExceptionHandler(value = IllegalArgumentException.class)
    ResponseEntity<APIResponse> handlingIllegalArgumentException(IllegalArgumentException exception)
    {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setCode(ErrorCode.MongoSecurityException.getCode());
        apiResponse.setMessage(ErrorCode.MongoSecurityException.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<APIResponse> handlingAppException(MethodArgumentNotValidException exception)
    {
        String enumkey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(enumkey);
        APIResponse apiResponse = new APIResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }


}
