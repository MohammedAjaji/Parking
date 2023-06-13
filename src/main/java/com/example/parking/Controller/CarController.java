package com.example.parking.Controller;

import com.example.parking.ApiResponse.ApiResponse;
import com.example.parking.Model.Car;
import com.example.parking.Service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/car")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping("/get")
    public ResponseEntity getAllCar(){
        List<Car> cars=carService.getAllCars();
        return ResponseEntity.status(200).body(cars);
    }

    @PostMapping("/add")
    public ResponseEntity addCar(@Valid @RequestBody Car car){
        carService.addCar(car);
        return ResponseEntity.status(200).body(new ApiResponse("Car added"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateCar(@Valid @RequestBody Car car, @Valid @PathVariable Integer id){
        carService.updateCar(car,id);
        return ResponseEntity.status(200).body("Car updated");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteCar(@PathVariable Integer id){
        carService.deleteCar(id);
        return ResponseEntity.status(200).body("Car deleted");
    }

}
