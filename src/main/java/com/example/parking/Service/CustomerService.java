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

import java.util.ArrayList;
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

    public Customer addCustomer(CustomerDTO customerDTO){

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
        customer.setPoints(0.0);
        customer.setFine(0.0);

        user.setCustomer(customer);
        customer.setUser(user);

        myUserRepository.save(user);
//         customerRepository.save(customer);
        return customerRepository.save(customer);
    }


    public void updateCustomer(MyUser user,Integer customerId, CustomerDTO customerDTO){
        Customer oldCustomer = customerRepository.findCustomerById(customerId);

        if (!Objects.equals(user.getId(), customerId)){
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

        if (!Objects.equals(user.getId(), customerId)){
            throw new ApiException("Not Authorized");
        }

        if (customer == null){
            throw new ApiException("customer Not found");
        }

        List<Car> cars = carRepository.findCarByCustomer(customer);

        for (int i = 0; i < cars.size(); i++) {
            List<Booking> bookings = bookingRepository.findAllByCar(cars.get(i));
            for (int k = 0; k < bookings.size(); k++) {
                if (bookings.get(k).getStatus().equalsIgnoreCase("new") || bookings.get(k).getStatus().equalsIgnoreCase("active")) {
                    throw new ApiException("Cannot Delete cars where there are Bookings");
                }
                bookings.get(k).setCar(null);
            }
            cars.get(i).setCustomer(null);
            carRepository.delete(cars.get(i));
        }
        MyUser oldUser = myUserRepository.findMyUserById(user.getId());

        customerRepository.delete(oldUser.getCustomer());
        myUserRepository.delete(oldUser);

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

        MyUser newUser = myUserRepository.findMyUserById(user.getId());
        return newUser;
    }

    public double payFine(MyUser user, double amount){
        Customer customer= customerRepository.findCustomerByUser(user);
        if(customer==null){
            throw new ApiException("customer not found");
        }
        if (amount % 50 != 0){
            throw new ApiException("amount has to be Multiples of 50");
        }
        if (customer.getFine() == 0){
            throw new ApiException("you are clean don't worry ^_^");
        }
        double fine = customer.getFine() - amount;
        customer.setFine(fine);
        customerRepository.save(customer);
        return fine;
    }
    public List<Car> getCustomerBookings(MyUser user){
        Customer customer = customerRepository.findCustomerByUser(user);
        if (customer == null) {
            throw new ApiException("Not Authorized");
        }
        List<Car> cars = carRepository.findCarByCustomer(customer);
        List<Car> carList = new ArrayList<>();
        for (int i = 0; i < cars.size(); i++) {
            List<Booking> bookings = bookingRepository.findAllByCar(cars.get(i));
            if (bookings.get(i) != null && !(bookings.get(i).getStatus().equalsIgnoreCase("expired"))){
                carList.add(bookings.get(i).getCar());
            }
        }
        return carList;
    }

    public List<Car> getCustomerOldBookings(MyUser user){
        Customer customer = customerRepository.findCustomerByUser(user);
        if (customer == null) {
            throw new ApiException("Not Authorized");
        }
        List<Car> cars = carRepository.findCarByCustomer(customer);
        List<Car> carList = new ArrayList<>();
        for (int i = 0; i < cars.size(); i++) {
            List<Booking> bookings = bookingRepository.findAllByCar(cars.get(i));
            if (bookings.get(i) != null && bookings.get(i).getStatus().equalsIgnoreCase("expired")){
                carList.add(bookings.get(i).getCar());
            }
        }
        return carList;
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
