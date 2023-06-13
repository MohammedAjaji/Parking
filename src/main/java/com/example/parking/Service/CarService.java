package com.example.parking.Service;

import com.example.parking.ApiException.ApiException;
import com.example.parking.Model.Car;
import com.example.parking.Repository.CarRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    public List<Car> getAllCars() {
        List<Car> cars = carRepository.findAll();
        return cars;
    }

    public void addCar(Car car) {
        carRepository.save(car);
    }

    public void updateCar(Car car,Integer id){
        Car oldCar= carRepository.findCarById(id);

        if(oldCar==null){
            throw new ApiException("Car not found");
        }
        oldCar.setName(car.getName());
        oldCar.setLicensePlate(car.getLicensePlate());
        oldCar.setColor(car.getColor());
        oldCar.setHandicap(car.getHandicap());

        carRepository.save(oldCar);
    }

    public void deleteCar(Integer id){
        Car car= carRepository.findCarById(id);
        if(car==null){
            throw new ApiException("Car not found");
        }

        carRepository.delete(car);
    }

}
