package com.example.parking.Service;

import com.example.parking.ApiException.ApiException;
import com.example.parking.Model.*;
import com.example.parking.Repository.CarRepository;
import com.example.parking.Repository.CompanyRepository;
import com.example.parking.Repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;


    public List<Car> getCars() {
        return carRepository.findAll();
    }

    public void addCar(MyUser user,Car car) {
        Customer customer = customerRepository.findCustomerByUser(user);
        if (customer == null){
            throw new ApiException("Sorry Only Customer can add Car");
        }
        car.setCustomer(customer);
        carRepository.save(car);
    }

    public void updateCar(MyUser user,Car car,Integer carId){

        Customer customer = customerRepository.findCustomerByUser(user);
        if (car == null){
            throw new ApiException("Sorry Only Customer can update car");
        }

        Car oldCar = carRepository.findCarById(carId);
        if (oldCar == null){
            throw new ApiException("Car Not Found");
        }

        oldCar.setName(car.getName());
        oldCar.setLicensePlate(car.getLicensePlate());
        oldCar.setColor(car.getColor());
        oldCar.setHandicap(car.getHandicap());

        carRepository.save(oldCar);
    }

    public void deleteCar(MyUser user, Integer carId){
        Customer customer= user.getCustomer();
        if(customer==null){
            throw new ApiException("Sorry Only Customers can delete Car");
        }
        Car car = carRepository.findCarById(carId);
        if (car == null){
            throw new ApiException("Car Not Found");
        }
        carRepository.delete(car);
    }

}
