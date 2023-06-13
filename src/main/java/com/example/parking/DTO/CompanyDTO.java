package com.example.parking.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyDTO {

    private String username;

    private String password;

    private String name;

    private String status;

    private Double revenue;
}
