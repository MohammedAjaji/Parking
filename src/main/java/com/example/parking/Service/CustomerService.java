package com.example.parking.Service;

import com.example.parking.ApiException.ApiException;
import com.example.parking.DTO.CustomerDTO;
import com.example.parking.Model.Company;
import com.example.parking.Model.Customer;
import com.example.parking.Model.MyUser;
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


    public List<Customer> getAllCustomer(){
        List<Customer> customers =  customerRepository.findAll();
        return customers;
    }

    public void addCustomer(CustomerDTO customerDTO){

        String hash = new BCryptPasswordEncoder().encode(customerDTO.getPassword());
        MyUser user = new MyUser();
        user.setUsername(customerDTO.getUsername());
        user.setPassword(hash);
        user.setRole("CUSTOMER");

        Customer customer = new Customer();
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setPhoneNum(customerDTO.getPhoneNum());
        customer.setBalance(customerDTO.getBalance());

        user.setCustomer(customer);

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
        Customer oldCustomer = customerRepository.findCustomerById(customerId);

        if (!Objects.equals(user.getCustomer().getId(), customerId)){
            throw new ApiException("Not Authorized");
        }

        if (oldCustomer == null){
            throw new ApiException("customer Not found");
        }

        customerRepository.delete(oldCustomer);
        myUserRepository.delete(user);

    }

}
