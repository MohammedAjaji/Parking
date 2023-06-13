package com.example.parking.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerDTO {

    private String username;
    private String password;

    private String firstName;
    private String lastName;

    private String phoneNum;

    private Double balance;


}
