package com.example.parking.Service;

import com.example.parking.ApiException.ApiException;
import com.example.parking.Model.Customer;
import com.example.parking.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<Customer> getAllCustomer(){
        List<Customer> customers =  customerRepository.findAll();
        return customers;
    }

    public void addCustomer(Customer customer){
        customerRepository.save(customer);
    }


    public void updateCustomer(Integer id, Customer customer){
        Customer oldCustomer = customerRepository.findCustomerById(id);
        if (oldCustomer==null){
            throw new ApiException("Customer not found");
        }
        oldCustomer.setFirstName(customer.getFirstName());
        oldCustomer.setLastName(customer.getLastName());
        oldCustomer.setPhoneNum(customer.getPhoneNum());
        oldCustomer.setBalance(customer.getBalance());

        customerRepository.save(oldCustomer);
    }

    public void deleteCustomer(Integer id){
        Customer oldCustomer = customerRepository.findCustomerById(id);
        if (oldCustomer==null){
            throw new ApiException("Customer not found");
        }
        customerRepository.delete(oldCustomer);
    }

}
