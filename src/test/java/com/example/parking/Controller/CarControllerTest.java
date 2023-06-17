package com.example.parking.Controller;

import com.example.parking.Controller.CarController;
import com.example.parking.Model.Car;
import com.example.parking.Model.MyUser;
import com.example.parking.Service.CarService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CarControllerTest {

    @Mock
    private CarService carService;

    @InjectMocks
    private CarController carController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCar_shouldReturnListOfCars() {
        // Arrange
        List<Car> cars = new ArrayList<>();
        cars.add(new Car());
        cars.add(new Car());
        Mockito.when(carService.getCars()).thenReturn(cars);

        // Act
        ResponseEntity<List<Car>> response = carController.getCar();

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(cars, response.getBody());
        Mockito.verify(carService, Mockito.times(1)).getCars();
    }

    @Test
    void addCar_shouldReturnCarAddedMessage() {
        // Arrange
        MyUser user = new MyUser();
        Car car = new Car();

        // Act
        ResponseEntity<String> response = carController.addCar(user, car);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Car added", response.getBody());
        Mockito.verify(carService, Mockito.times(1)).addCar(user, car);
    }

    @Test
    void updateCar_shouldReturnCarUpdatedMessage() {
        // Arrange
        MyUser user = new MyUser();
        Car car = new Car();
        Integer carId = 1;

        // Act
        ResponseEntity<String> response = carController.updateCar(user, car, carId);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Car updated", response.getBody());
        Mockito.verify(carService, Mockito.times(1)).updateCar(user, car, carId);
    }

    @Test
    void deleteCar_shouldReturnCarDeletedMessage() {
        // Arrange
        MyUser user = new MyUser();
        Integer carId = 1;

        // Act
        ResponseEntity<String> response = carController.deleteCar(user, carId);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Car deleted", response.getBody());
        Mockito.verify(carService, Mockito.times(1)).deleteCar(user, carId);
    }
}

