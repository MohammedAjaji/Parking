package com.example.parking.Controller;

import com.example.parking.DTO.CompanyDTO;
import com.example.parking.DTO.CustomerDTO;
import com.example.parking.Model.Car;
import com.example.parking.Model.Company;
import com.example.parking.Model.Customer;
import com.example.parking.Model.MyUser;
import com.example.parking.Service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/get")
    public ResponseEntity getAllCustomer(){
        List<Customer> customers= customerService.getAllCustomer();
        return ResponseEntity.status(200).body(customers);
    }

    @PutMapping("update/{customerId}")
    public ResponseEntity updateCustomer(@AuthenticationPrincipal MyUser user, @PathVariable Integer customerId,@RequestBody CustomerDTO customerDTO){
        customerService.updateCustomer(user, customerId, customerDTO);
        return ResponseEntity.status(200).body("Customer Updated");
    }

    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity deleteCustomer(@AuthenticationPrincipal MyUser user, @PathVariable Integer customerId){
        customerService.deleteCustomer(user,customerId);
        return ResponseEntity.status(200).body("Customer deleted");
    }

    @GetMapping("/get-cars")
    public ResponseEntity getCustomrsCars(@AuthenticationPrincipal MyUser user){
        List<Car> cars= customerService.getCustomrsCars(user);
        return ResponseEntity.status(200).body(cars);
    }

}
