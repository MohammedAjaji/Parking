package com.example.parking.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsernameDTO {

    @NotEmpty(message = "username cannot be empty ")
    private String username;
}

