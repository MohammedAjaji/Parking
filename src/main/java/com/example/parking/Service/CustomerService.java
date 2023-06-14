package com.example.parking.Service;

import com.example.parking.ApiException.ApiException;
import com.example.parking.DTO.CustomerDTO;
import com.example.parking.Model.*;
import com.example.parking.Repository.BookingRepository;
import com.example.parking.Repository.CarRepository;
import com.example.parking.Repository.CustomerRepository;
import com.example.parking.Repository.MyUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final MyUserRepository myUserRepository;
    private final CarRepository carRepository;
    private final BookingRepository bookingRepository;


    public List<Customer> getAllCustomer(){
        List<Customer> customers =  customerRepository.findAll();
        return customers;
    }

    public void addCustomer(CustomerDTO customerDTO){

        String hash = new BCryptPasswordEncoder().encode(customerDTO.getPassword());
        MyUser user = new MyUser();
        user.setUsername(customerDTO.getUsername());
        user.setPassword(hash);
        user.setEmail(customerDTO.getEmail());
        user.setRole("CUSTOMER");

        Customer customer = new Customer();
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setPhoneNum(customerDTO.getPhoneNum());
        customer.setBalance(customerDTO.getBalance());

        user.setCustomer(customer);
        customer.setUser(user);

        myUserRepository.save(user);
        customerRepository.save(customer);
    }


    public void updateCustomer(MyUser user,Integer customerId, CustomerDTO customerDTO){
        Customer oldCustomer = customerRepository.findCustomerById(customerId);

        if (!Objects.equals(user.getCustomer().getId(), customerId)){
            throw new ApiException("Not Authorized");
        }

        if (oldCustomer == null){
            throw new ApiException("customer Not found");
        }

        oldCustomer.setFirstName(customerDTO.getFirstName());
        oldCustomer.setLastName(customerDTO.getLastName());
        oldCustomer.setPhoneNum(customerDTO.getPhoneNum());
        oldCustomer.setBalance(customerDTO.getBalance());

        customerRepository.save(oldCustomer);
    }

    public void deleteCustomer(MyUser user,Integer customerId){
        Customer customer = customerRepository.findCustomerById(customerId);

        if (!Objects.equals(user.getCustomer().getId(), customerId)){
            throw new ApiException("Not Authorized");
        }

        if (customer == null){
            throw new ApiException("customer Not found");
        }

        List<Car> cars = carRepository.findCarByCustomer(customer);

        for (int i = 0; i < cars.size(); i++) {
            cars.get(i).setCustomer(null);
            carRepository.delete(cars.get(i));
        }

        customerRepository.delete(customer);
        myUserRepository.delete(user);

    }

    public List<Car> getCustomrCars(MyUser user){
        Customer customer = customerRepository.findCustomerByUser(user);
        if (customer == null) {
            throw new ApiException("Not Authorized");
        }
        List<Car> cars = carRepository.findCarByCustomer(customer);
        return cars;
    }

    //endpoint that takes customer id and return All customer details
    public MyUser getCustomerDetails(MyUser user){
        Customer customer= customerRepository.findCustomerByUser(user);
        if(customer==null){
            throw new ApiException("customer not found");
        }
        return user;
    }

//    public List<Booking> getCustomerBooking(MyUser user){
//        Customer customer = customerRepository.findCustomerByUser(user);
//        if (customer == null) {
//            throw new ApiException("Not Authorized");
//        }
//        List<Car> cars = carRepository.findCarByCustomer(customer);
//        for (int i = 0; i < cars.size(); i++) {
//            List<Booking> booking = bookingRepository.findBookingByCar(cars.get(i));
//
//        }
//
//    }

}
