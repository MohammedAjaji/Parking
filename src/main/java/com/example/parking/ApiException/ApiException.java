package com.example.parking.ApiException;

import com.example.parking.ApiResponse.ApiResponse;

public class ApiException extends RuntimeException{
    public ApiException (String message){
        super(message);
    }
}
