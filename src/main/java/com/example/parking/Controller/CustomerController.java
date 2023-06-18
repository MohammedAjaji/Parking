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
        List<Car> cars= customerService.getCustomrCars(user);
        return ResponseEntity.status(200).body(cars);
    }

    @GetMapping("/details")
    public ResponseEntity getCustomerDetails(@AuthenticationPrincipal MyUser user){
        MyUser myUser =customerService.getCustomerDetails(user);
        return ResponseEntity.status(200).body(myUser);
    }

    @PutMapping("/pay-fine")
    public ResponseEntity payFine(@AuthenticationPrincipal MyUser user, @RequestBody double amount){
        double fine =  customerService.payFine(user, amount);
        return ResponseEntity.status(200).body("Pay Fine is done " +
                "\n your fine is now " + fine);
    }

    @GetMapping("/get-booking")
    public ResponseEntity getCustomerBookings(@AuthenticationPrincipal MyUser user){
        List<Car> cars = customerService.getCustomerBookings(user);
        return ResponseEntity.status(200).body(cars);
    }

    @GetMapping("/get-old-booking")
    public ResponseEntity getCustomerOldBookings(@AuthenticationPrincipal MyUser user){
        List<Car> cars = customerService.getCustomerOldBookings(user);
        return ResponseEntity.status(200).body(cars);
    }

}
