package com.example.parking.Repository;

import com.example.parking.DTO.CustomerDTO;
import com.example.parking.Model.Car;
import com.example.parking.Model.Customer;
import com.example.parking.Model.MyUser;
import com.example.parking.Service.CustomerService;
import org.apache.catalina.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CustomerRepoTest {




    @Autowired
    private CustomerRepository customerRepository;


    @Autowired
    private MyUserRepository myUserRepository;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @BeforeEach
    public void setup() {


    }

    @Test
    public void testFindCustomerById() {
        // Perform the addCustomer operation
        MyUser user1 = new MyUser(null, "user1", passwordEncoder.encode("password1"), "user1@example.com", "CUSTOMER", null, null);
        MyUser user=myUserRepository.save(user1);
        Customer customerDTO = new Customer();
        customerDTO.setFirstName("mohammed");
        customerDTO.setBalance(34354.456);
        customerDTO.setPhoneNum("+966XXXXXXX");
        customerDTO.setLastName("alyami");
        customerDTO.setUser(user);
        Customer customer=customerRepository.save(customerDTO);

        Customer testCustomer = customerRepository.findCustomerById(customer.getId());
        assertNotNull(testCustomer);
    }

    @Test
    public void testFindCustomerByUser() {
        // Perform the updateCustomer operation
        Customer customerDTO = new Customer();
        customerDTO.setFirstName("Updated");
        customerDTO.setLastName("Customer");
        customerDTO.setBalance(34354.456);
        customerDTO.setPhoneNum("+966XXXXXXX");
        customerDTO.setPhoneNum("123456789");
        customerDTO.setBalance(500.0);

        MyUser user = new MyUser(null, "user1", passwordEncoder.encode("password1"), "user1@example.com", "COMPANY", null, null);
        MyUser savedU=myUserRepository.save(user);
        customerDTO.setUser(savedU);
        Customer customer=customerRepository.save(customerDTO);
        // Verify that the customer is updated in the database
        Customer customerUser=customerRepository.findCustomerByUser(savedU);
        assertNotNull(customerUser);

    }



}

