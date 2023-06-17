package com.example.parking.Controller;

import com.example.parking.Controller.CustomerController;
import com.example.parking.DTO.CustomerDTO;
import com.example.parking.Model.Car;
import com.example.parking.Model.Customer;
import com.example.parking.Model.MyUser;
import com.example.parking.Service.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCustomer_shouldReturnListOfCustomers() {
        // Arrange
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer());
        customers.add(new Customer());
        Mockito.when(customerService.getAllCustomer()).thenReturn(customers);

        // Act
        ResponseEntity<List<Customer>> response = customerController.getAllCustomer();

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(customers, response.getBody());
        Mockito.verify(customerService, Mockito.times(1)).getAllCustomer();
    }

    @Test
    void updateCustomer_shouldReturnCustomerUpdatedMessage() {
        // Arrange
        MyUser user = new MyUser();
        Integer customerId = 1;
        CustomerDTO customerDTO = new CustomerDTO("mohammed","mohammed@123",
                "mohammed@gmail.com","mohammed","alyami"
                ,"+96665856328",99.99);

        // Act
        ResponseEntity<String> response = customerController.updateCustomer(user, customerId, customerDTO);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Customer Updated", response.getBody());
        Mockito.verify(customerService, Mockito.times(1)).updateCustomer(user, customerId, customerDTO);
    }

    @Test
    void deleteCustomer_shouldReturnCustomerDeletedMessage() {
        // Arrange
        MyUser user = new MyUser();
        Integer customerId = 1;

        // Act
        ResponseEntity<String> response = customerController.deleteCustomer(user, customerId);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Customer deleted", response.getBody());
        Mockito.verify(customerService, Mockito.times(1)).deleteCustomer(user, customerId);
    }

    @Test
    void getCustomrsCars_shouldReturnListOfCars() {
        // Arrange
        MyUser user = new MyUser();
        List<Car> cars = new ArrayList<>();
        cars.add(new Car());
        cars.add(new Car());
        Mockito.when(customerService.getCustomrCars(user)).thenReturn(cars);

        // Act
        ResponseEntity<List<Car>> response = customerController.getCustomrsCars(user);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(cars, response.getBody());
        Mockito.verify(customerService, Mockito.times(1)).getCustomrCars(user);
    }

    @Test
    void getCustomerDetails_shouldReturnUserDetails() {
        // Arrange
        MyUser user = new MyUser();
        MyUser userDetails = new MyUser();
        Mockito.when(customerService.getCustomerDetails(user)).thenReturn(userDetails);

        // Act
        ResponseEntity<MyUser> response = customerController.getCustomerDetails(user);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(userDetails, response.getBody());
        Mockito.verify(customerService, Mockito.times(1)).getCustomerDetails(user);
    }
}

