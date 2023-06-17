package com.example.parking.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CompanyDTO {

    private String username;
    private String password;
    private String email;

    private String name;

    private String status;

    private Double revenue;
}
