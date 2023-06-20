package com.example.parking.Service;

import com.example.parking.DTO.CustomerDTO;
import com.example.parking.Model.*;
import com.example.parking.Repository.BookingRepository;
import com.example.parking.Repository.CarRepository;
import com.example.parking.Repository.CustomerRepository;
import com.example.parking.Repository.MyUserRepository;
import com.example.parking.Service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CustomerServiceTest {


    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private Customer testCustomer;
    private MyUser testUser;

    @BeforeEach
    public void setup() {
        // Create and save test data before each test method
        // For example, create a test customer and user
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName("Mohammed");
        customerDTO.setBalance(34354.456);
        customerDTO.setPhoneNum("+966XXXXXXX");
        customerDTO.setLastName("Alaymi");
        customerDTO.setUsername("Mohammed");
        customerDTO.setPassword("mohammed@123");
        customerDTO.setEmail("Mohammed@gmail.com");
        customerService=new CustomerService(customerRepository,myUserRepository,carRepository,bookingRepository);
        Customer customer=customerService.addCustomer(customerDTO);

        testCustomer = customerRepository.findCustomerById(customer.getId());
        testUser = myUserRepository.findMyUserByUsername(customer.getUser().getUsername());
    }

    @Test
    public void testAddCustomer() {
        // Perform the addCustomer operation
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName("Jane");
        customerDTO.setBalance(34354.456);
        customerDTO.setPhoneNum("+966XXXXXXX");
        customerDTO.setLastName("Smith");
        customerDTO.setUsername("janesmith");
        customerDTO.setPassword("testpassword");
        customerDTO.setEmail("janesmith@example.com");
        Customer customerS=customerService.addCustomer(customerDTO);

        // Verify that the customer and user are created and saved in the database
        Customer customer =  customerRepository.findCustomerById(customerS.getId());
        assertNotNull(customer);
        MyUser user = myUserRepository.findMyUserByUsername(customer.getUser().getUsername());
        assertNotNull(user);
    }

    @Test
    public void testUpdateCustomer() {
        // Perform the updateCustomer operation
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName("Updated");
        customerDTO.setLastName("Customer");
        customerDTO.setBalance(34354.456);
        customerDTO.setPhoneNum("+966XXXXXXX");
        customerDTO.setPhoneNum("123456789");
        customerDTO.setBalance(500.0);

        customerService.updateCustomer(testUser, testCustomer.getId(), customerDTO);

        // Verify that the customer is updated in the database
        Customer updatedCustomer = customerRepository.findById(testCustomer.getId()).orElse(null);
        assertNotNull(updatedCustomer);
        assertEquals("Updated", updatedCustomer.getFirstName());
        assertEquals("Customer", updatedCustomer.getLastName());
        assertEquals("123456789", updatedCustomer.getPhoneNum());
        assertEquals(500.0, updatedCustomer.getBalance());
    }

    @Test
    public void testDeleteCustomer() {
        // Perform the deleteCustomer operation
        customerService.deleteCustomer(testUser, testCustomer.getId());

        // Verify that the customer and user are deleted from the database
        Customer deletedCustomer = customerRepository.findById(testCustomer.getId()).orElse(null);
        MyUser deletedUser = myUserRepository.findMyUserByUsername(testUser.getUsername());

        assertNull(deletedCustomer);
        assertNull(deletedUser);
    }

    @Test
    public void testGetCustomerCars() {
        // Perform the getCustomerCars operation
        List<Car> cars = customerService.getCustomrCars(testUser);

        // Verify that the returned cars belong to the customer
        for (Car car : cars) {
            assertEquals(testCustomer, car.getCustomer());
        }
    }

    @Test
    public void testGetCustomerDetails() {
        // Perform the getCustomerDetails operation
        MyUser customerUser = customerService.getCustomerDetails(testUser);

        // Verify that the returned user is associated with the customer
        assertEquals(testCustomer, customerUser.getCustomer());
    }

    // Write more test methods for other scenarios

}

