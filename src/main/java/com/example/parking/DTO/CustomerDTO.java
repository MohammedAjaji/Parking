package com.example.parking.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    @NotEmpty(message = "name cannot be empty ")
    private String username;

    @NotEmpty(message = "password cannot be empty ")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$", message = "Password should be Minimum eight characters, at least one uppercase letter, one lowercase letter and one number")
    private String password;

    @NotEmpty(message = "email cannot be empty ")
    @Email
    private String email;

    @NotEmpty(message = "firstName cannot be empty ")
    private String firstName;

    @NotEmpty(message = "lastName cannot be empty ")
    private String lastName;

    @NotEmpty(message = "lastName cannot be empty ")
    private String phoneNum;

    @NotNull(message ="balance should not be empty" )
    @Positive(message = "please enter positive number")
    private Double balance;


}
