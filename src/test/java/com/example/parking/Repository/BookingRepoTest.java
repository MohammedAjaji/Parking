package com.example.parking.Repository;

import com.example.parking.Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class BookingRepoTest {




    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private TimeRepository timeRepository;

    private Customer testCustomer;
    private MyUser testUser;
    @Autowired
    private ParkingRepository parkingRepository;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @BeforeEach
    public void setup() {
        // Create and save test data before each test method
        // For example, create a test customer and user


    }

    @Test
    public void findBookingById() {
        // Perform the addCustomer operation
        Time time=new Time();
        Time savedTime=timeRepository.save(time);
        Booking booking=new Booking();
        booking.setTotalPrice(564354.54);
        booking.setTime(savedTime);
        Booking booking1=bookingRepository.save(booking);

        Booking test = bookingRepository.findBookingById(booking1.getId());
        assertNotNull(test);
    }

    @Test
    public void findAllByParking() {
        // Perform the updateCustomer operation
        Time time=new Time();
        Time savedTime=timeRepository.save(time);
        Parking parking =new Parking();
        parking.setParkingNumber("P343511");
        parking.setPrice(654364.435);
        parking.setFloor(354354);

        parkingRepository.save(parking);
        Booking booking=new Booking();
        booking.setTotalPrice(564354.54);
        booking.setParking(parking);
        booking.setTime(savedTime);
        Booking booking1=bookingRepository.save(booking);
        // Verify that the customer is updated in the database
        List<Booking> test = bookingRepository.findAllByParking(parking);
        assertNotNull(test);

    }



}

