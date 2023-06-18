package com.example.parking.Repository;

import com.example.parking.DTO.CompanyDTO;
import com.example.parking.Model.Company;
import com.example.parking.Model.Customer;
import com.example.parking.Model.MyUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CompanyRepoTest {




    @Autowired
    private CompanyRepository companyRepository;



    @Autowired
    private MyUserRepository myUserRepository;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @BeforeEach
    public void setup() {
        // Create and save test data before each test method
        // For example, create a test customer and user


    }

    @Test
    public void testFindCompanyById() {
        // Perform the addCustomer operation
        MyUser user1 = MyUser.builder().username("user1").password(passwordEncoder.encode("password1")).email("user1@example.com")
                .role( "CUSTOMER").build();
        MyUser user=myUserRepository.save(user1);
        Company companyDTO = new Company();
        companyDTO.setName("New Company");
        companyDTO.setRevenue(435.4354);
        companyDTO.setStatus("pending");
        companyDTO.setUser(user);
        Company customer=companyRepository.save(companyDTO);

        Company testCustomer = companyRepository.findCompanyById(customer.getId());
        assertNotNull(testCustomer);
    }

    @Test
    public void testFindCustomerByUser() {
        // Perform the updateCustomer operation
        Company companyDTO = new Company();
        companyDTO.setName("New Company");
        companyDTO.setRevenue(435.4354);
        companyDTO.setStatus("pending");

        MyUser user1 = MyUser.builder().username("user1").password(passwordEncoder.encode("password1")).email("user1@example.com")
                .role( "CUSTOMER").build();
        MyUser savedU=myUserRepository.save(user1);
        companyDTO.setUser(savedU);
        Company Company=companyRepository.save(companyDTO);

        Company testCompany = companyRepository.findCompanyByUser(savedU);
        assertNotNull(testCompany);

    }



}

