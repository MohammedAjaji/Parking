package com.example.parking.Repository;

import com.example.parking.Model.Branch;
import com.example.parking.Model.Car;
import com.example.parking.Model.Company;
import com.example.parking.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {

    Car findCarById(Integer id);

    Car findCarByCustomer(Customer customer);

}
