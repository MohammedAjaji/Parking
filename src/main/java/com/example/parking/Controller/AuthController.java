package com.example.parking.Controller;


import com.example.parking.DTO.CompanyDTO;
import com.example.parking.DTO.CustomerDTO;
import com.example.parking.Model.MyUser;
import com.example.parking.Service.CompanyService;
import com.example.parking.Service.CustomerService;
import com.example.parking.Service.MyUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MyUserService myUserService;
    private final CompanyService companyService;
    private final CustomerService customerService;

    @GetMapping("/get-users")
    public ResponseEntity getUsers(){
        List<MyUser> users = myUserService.getUsers();
        return ResponseEntity.status(200).body(users);
    }

    @PostMapping("/register/admin")
    public ResponseEntity registerAdmin(@Valid @RequestBody MyUser user){
        myUserService.addAdmin(user);
        return ResponseEntity.status(200).body("Admin Registered");
    }

    @PostMapping("/register/company")
    public ResponseEntity registerCompany(@Valid @RequestBody CompanyDTO companyDTO){
        companyService.addCompany(companyDTO);
        return ResponseEntity.status(200).body("Company Registered");
    }

    @PostMapping("/register/customer")
    public ResponseEntity registerCustomer(@Valid @RequestBody CustomerDTO customerDTO){
        customerService.addCustomer(customerDTO);
        return ResponseEntity.status(200).body("Customer Registered");
    }

    @PutMapping("/update-password")
    public ResponseEntity updateUserPassword(@AuthenticationPrincipal MyUser myUser, @RequestBody String password){
        myUserService.updateUserPassword(myUser, password);
        return ResponseEntity.status(200).body("Password Updated");
    }

    @PutMapping("/update-username")
    public ResponseEntity updateUserUsername(@AuthenticationPrincipal MyUser myUser, @RequestBody String username){
        myUserService.updateUserUsername(myUser, username);
        return ResponseEntity.status(200).body("Username Updated");
    }

    @PostMapping("/admin")
    public ResponseEntity admin(){
        return ResponseEntity.status(200).body("Welcome Admin");
    }

    @PostMapping("/customer")
    public ResponseEntity customer(){
        return ResponseEntity.status(200).body("Welcome Customer");
    }

    @PostMapping("/company")
    public ResponseEntity company(){
        return ResponseEntity.status(200).body("Welcome Customer");
    }


    @PostMapping("/login")
    public ResponseEntity login(){
        return ResponseEntity.status(200).body("Login");
    }

    @PostMapping("/logout")
    public ResponseEntity logout(){
        return ResponseEntity.status(200).body("Logout");
    }

}
