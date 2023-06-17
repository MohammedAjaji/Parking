package com.example.parking.Repository;

import com.example.parking.Model.Car;
import com.example.parking.Model.Customer;
import com.example.parking.Model.MyUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CarRepoTest {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private MyUserRepository myUserRepository;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void testFindCarById(){
        Car car = new Car();
        car.setName("BMW");
        car.setColor("seffs");
        car.setLicensePlate("dsgsgd");
        Car car1=carRepository.save(car);

        Car carTest=carRepository.findCarById(car1.getId());
        assertEquals(carTest,car1);
    }

    @Test
    public void testFindCarByCustomer() {
        // Perform the addCustomer operation
        MyUser user1 = new MyUser(null, "user1", passwordEncoder.encode("password1"), "user1@example.com", "CUSTOMER", null, null, null);
        MyUser user=myUserRepository.save(user1);
        Customer customerDTO = new Customer();
        customerDTO.setFirstName("mohammed");
        customerDTO.setBalance(34354.456);
        customerDTO.setPhoneNum("+213XXXXXXX");
        customerDTO.setLastName("alyami");
        customerDTO.setUser(user);
        Customer customer=customerRepository.save(customerDTO);
        Car car = new Car();
        car.setName("BMW");
        car.setColor("seffs");
        car.setLicensePlate("dsgsgd");
        car.setCustomer(customer);
        Car car1=carRepository.save(car);
        List<Car> testCars = carRepository.findCarByCustomer(customer);
        assertNotNull(testCars);
        assertEquals(1,testCars.size());
    }
}
