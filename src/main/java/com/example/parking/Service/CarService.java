package com.example.parking.Service;

import com.example.parking.ApiException.ApiException;
import com.example.parking.Model.*;
import com.example.parking.Repository.BookingRepository;
import com.example.parking.Repository.CarRepository;
import com.example.parking.Repository.CompanyRepository;
import com.example.parking.Repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;


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
        if (!Objects.equals(car.getCustomer().getUser().getId(), customer.getUser().getId())){
            throw new ApiException("Not Authorized");
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
        if (!Objects.equals(car.getCustomer().getUser().getId(), customer.getUser().getId())){
            throw new ApiException("Not Authorized");
        }
        List<Booking> bookings = bookingRepository.findAllByCar(car);
        for (int k = 0; k < bookings.size(); k++) {
            if (bookings.get(k).getStatus().equalsIgnoreCase("new") || bookings.get(k).getStatus().equalsIgnoreCase("active")) {
                throw new ApiException("Cannot Delete cars where there are Bookings");
            }
            bookings.get(k).setCar(null);
        }
        carRepository.delete(car);
    }

}
