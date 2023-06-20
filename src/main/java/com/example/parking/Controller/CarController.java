package com.example.parking.Controller;

import com.example.parking.ApiResponse.ApiResponse;
import com.example.parking.Model.Branch;
import com.example.parking.Model.Car;
import com.example.parking.Model.MyUser;
import com.example.parking.Service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/car")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping("/get")
    public ResponseEntity getCar(){
        List<Car> cars=carService.getCars();
        return ResponseEntity.status(200).body(cars);
    }

    @PostMapping("/add")
    public ResponseEntity addCar(@AuthenticationPrincipal MyUser user,@Valid @RequestBody Car car){
        carService.addCar(user,car);
        return ResponseEntity.status(200).body("Car added");
    }

    @PutMapping("/update/{carId}")
    public ResponseEntity updateCar(@AuthenticationPrincipal MyUser user,@Valid @RequestBody Car car, @Valid @PathVariable Integer carId){
        carService.updateCar(user,car,carId);
        return ResponseEntity.status(200).body("Car updated");
    }

    @DeleteMapping("/delete/{carId}")
    public ResponseEntity deleteCar(@AuthenticationPrincipal MyUser user,@PathVariable Integer carId){
        carService.deleteCar(user,carId);
        return ResponseEntity.status(200).body("Car deleted");
    }


}
