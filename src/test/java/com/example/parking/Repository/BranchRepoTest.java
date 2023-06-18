package com.example.parking.Repository;

import com.example.parking.DTO.CompanyDTO;
import com.example.parking.Model.Branch;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class BranchRepoTest {




    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private MyUserRepository myUserRepository;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @BeforeEach
    public void setup() {


    }

    @Test
    public void testFindBranchesByCompany() {
        // Perform the addCustomer operation
        MyUser user1 = MyUser.builder().username("user1").password(passwordEncoder.encode("password1")).email("user1@example.com")
                .role( "CUSTOMER").build();
        MyUser user=myUserRepository.save(user1);
        Company companyDTO = new Company();
        companyDTO.setName("New Company");
        companyDTO.setRevenue(435.4354);
        companyDTO.setStatus("pending");
        companyDTO.setUser(user);
        Company company=companyRepository.save(companyDTO);

        List<Branch> testBranches = branchRepository.findBranchesByCompany(company);
        assertNotNull(testBranches);
    }
    @Test
    public void testFindBranchById() {
        // Perform the addCustomer operation
        Branch branch= new Branch();
        branch.setName("branch");
        branch.setPhoneNum("1234567890");
        branch.setLocation("XXXXXXX");
        Branch branch1=branchRepository.save(branch);
        Branch testBranch = branchRepository.findBranchById(branch1.getId());
        assertNotNull(testBranch);
    }



}
