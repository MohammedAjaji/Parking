package com.example.parking.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDTO {

    @NotEmpty(message = "username cannot be empty ")
    private String username;

    @NotEmpty(message = "password cannot be empty ")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$", message = "Password should be Minimum eight characters, at least one uppercase letter, one lowercase letter and one number")
    private String password;

    @NotEmpty(message = "email cannot be empty ")
    @Email
    private String email;

    @NotEmpty(message = "name cannot be empty ")
    private String name;

    private String status;

    private Double revenue;
}
