package com.example.parking.Repository;

import com.example.parking.Model.Booking;
import com.example.parking.Model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
//    List<Booking> findBookingByCar(Car car);

}
