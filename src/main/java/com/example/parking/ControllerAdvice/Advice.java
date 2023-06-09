package com.example.parking.ControllerAdvice;


import com.example.parking.ApiException.ApiException;
import com.example.parking.ApiResponse.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class Advice {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity ApiException(ApiException exception){
        String message = exception.getMessage();
        return ResponseEntity.status(400).body(new ApiResponse(message));
    }
}
