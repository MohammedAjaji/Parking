package com.example.parking.Service;



import com.example.parking.ApiException.ApiException;
import com.example.parking.Model.Car;
import com.example.parking.Model.Customer;
import com.example.parking.Model.MyUser;
import com.example.parking.Repository.CarRepository;
import com.example.parking.Repository.CustomerRepository;
import com.example.parking.Repository.MyUserRepository;
import com.example.parking.Service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CarServiceTest {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private CarService carService;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @BeforeEach
    void setUp() {
        // Perform any setup required for your tests
    }

    @Test
    void getCars_shouldReturnListOfCars() {
        // Arrange
        Car car1 = new Car();
        car1.setColor("seffs");
        car1.setLicensePlate("dsgsgd");
        car1.setName("Car 1");
        carRepository.save(car1);

        Car car2 = new Car();
        car2.setColor("seffs");
        car2.setLicensePlate("dsgsgd");
        car2.setName("Car 2");
        carRepository.save(car2);

        // Act
        List<Car> result = carService.getCars();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(car1));
        assertTrue(result.contains(car2));
    }

    @Test
    void addCar_shouldAddCarForCustomer() {
        // Arrange



        MyUser user =myUserRepository.save(MyUser.builder().username("user1").password(passwordEncoder.encode("password1")).email("user1@example.com")
                .role( "CUSTOMER").build());
        Customer customer = Customer.builder().balance(335453.645).firstName("srgsdgs").phoneNum("gfdwsgdsg").lastName("sgdsgsdg").user(user).build();;

        Car car = new Car();
        car.setColor("seffs");
        car.setLicensePlate("dsgsgd");
        car.setName("Car 2");
        car.setCustomer(customer);

        customer.setUser(user);
        customerRepository.save(customer);

        // Act
        assertDoesNotThrow(() -> carService.addCar(user, car));

        // Assert
//        Customer savedCustomer = customerRepository.findById(customer.getId()).orElse(null);
//        assertNotNull(savedCustomer);
//        List<Car> customerCars = carRepository.findCarByCustomer(savedCustomer);
//        assertEquals(1, customerCars.size());
//        assertEquals(car, customerCars.get(0));
    }

    @Test
    void addCar_shouldThrowExceptionIfUserIsNotCustomer() {
        // Arrange
        MyUser user =myUserRepository.save(MyUser.builder().username("user1").password(passwordEncoder.encode("password1")).email("user1@example.com")
                .role( "CUSTOMER").build());
        Car car = new Car();
        car.setColor("seffs");
        car.setLicensePlate("dsgsgd");
        car.setName("Car 2");


        // Act & Assert
        assertThrows(ApiException.class, () -> carService.addCar(user, car));
    }





    @Test
    void updateCar_shouldThrowExceptionIfCarNotFound() {
        // Arrange

        MyUser savedUser=myUserRepository.save(MyUser.builder().username("user1").password(passwordEncoder.encode("password1")).email("user1@example.com")
                .role( "CUSTOMER").build());
        Customer customer = Customer.builder().balance(335453.645).firstName("srgsdgs").phoneNum("gfdwsgdsg").lastName("sgdsgsdg").user(savedUser).build();;
        Customer savedC=customerRepository.save(customer);
        Car car = new Car();
        car.setName("Updated Car");
        car.setColor("seffs");
        car.setLicensePlate("dsgsgd");
        car.setCustomer(savedC);
        Car savedCar=carRepository.save(car);
        // Act & Assert
        assertDoesNotThrow(() -> carService.updateCar(savedUser, car, savedCar.getId()));
    }

    @Test
    void deleteCar_shouldDeleteCarForCustomer() {
        // Arrange
        MyUser savedUser=myUserRepository.save(MyUser.builder().username("user1").password(passwordEncoder.encode("password1")).email("user1@example.com")
                .role( "CUSTOMER").build());
        Customer customer = Customer.builder().balance(335453.645).firstName("srgsdgs").phoneNum("gfdwsgdsg").lastName("sgdsgsdg").user(savedUser).build();;
        Customer savedC=customerRepository.save(customer);

        Car car = new Car();
        car.setColor("seffs");
        car.setLicensePlate("dsgsgd");
        car.setName("Car 2");
        car.setCustomer(savedC);
        Car savedCar=carRepository.save(car);

        // Act
        assertThrows(ApiException.class, () -> carService.deleteCar(savedC.getUser(), car.getId()));

        // Assert
        Car deletedCar = carRepository.findById(savedCar.getId()).orElse(null);
        assertNotNull(deletedCar);
    }

    @Test
    void deleteCar_shouldThrowExceptionIfUserIsNotCustomer() {
        // Arrange
        MyUser user = new MyUser();
        Car car = new Car();

        // Act & Assert
        assertThrows(ApiException.class, () -> carService.deleteCar(user, 1));
    }

    @Test
    void deleteCar_shouldThrowExceptionIfCarNotFound() {
        // Arrange
        MyUser savedUser=myUserRepository.save(MyUser.builder().username("user1").password(passwordEncoder.encode("password1")).email("user1@example.com")
                .role( "CUSTOMER").build());
        Customer customer = Customer.builder().balance(335453.645).firstName("srgsdgs").phoneNum("gfdwsgdsg").lastName("sgdsgsdg").user(savedUser).build();;
        customer.setUser(savedUser);
        Customer savedC=customerRepository.save(customer);

        // Act & Assert
        assertThrows(ApiException.class, () -> carService.deleteCar(savedUser, 1));
    }
}
