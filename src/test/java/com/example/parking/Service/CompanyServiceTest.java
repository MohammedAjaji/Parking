package com.example.parking.Service;
import com.example.parking.DTO.CompanyDTO;
import com.example.parking.Model.*;
import com.example.parking.Repository.*;
import com.example.parking.Service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CompanyServiceTest {


    private CompanyService companyService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private Company testCompany;
    private MyUser testUser;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @BeforeEach
    public void setup() {
        // Create and save test data before each test method
        // For example, create a test company and user
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setName("Test Company");
        companyDTO.setUsername("testuser");
        companyDTO.setPassword("testpassword");
        companyDTO.setEmail("test@example.com");
        companyService=new CompanyService(companyRepository,myUserRepository,branchRepository,parkingRepository,bookingRepository);
        Company savedCompany=companyService.addCompany(companyDTO);

        testCompany = companyRepository.findCompanyById(savedCompany.getId());
        testUser = myUserRepository.findMyUserByUsername(savedCompany.getUser().getUsername());
    }

    @Test
    public void testAddCompany() {
        // Perform the addCompany operation
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setName("New Company");
        companyDTO.setUsername("newuser");
        companyDTO.setPassword("newpassword");
        companyDTO.setEmail("new@example.com");

        Company savedCompany=companyService.addCompany(companyDTO);


        // Verify that the company and user are created and saved in the database
        Company company = companyRepository.findCompanyById(savedCompany.getId());
        assertNotNull(company);
        MyUser user = myUserRepository.findMyUserByUsername(savedCompany.getUser().getUsername());
        assertNotNull(user);
    }

    @Test
    public void testUpdateCompany() {
        // Perform the updateCompany operation
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setName("Updated Company");
        companyDTO.setRevenue(1000.0);

        companyService.updateCompany(testUser, companyDTO, testCompany.getId());

        // Verify that the company is updated in the database
        Company updatedCompany = companyRepository.findById(testCompany.getId()).orElse(null);
        assertNotNull(updatedCompany);
        assertEquals("Updated Company", updatedCompany.getName());
        assertEquals(1000.0, updatedCompany.getRevenue());
    }

    @Test
    public void testDeleteCompany() {
        // Perform the deleteCompany operation
        companyService.deleteCompany(testUser, testCompany.getId());

        // Verify that the company and user are deleted from the database
        Company deletedCompany = companyRepository.findById(testCompany.getId()).orElse(null);
        MyUser deletedUser = myUserRepository.findMyUserByUsername(testUser.getUsername());

        assertNull(deletedCompany);
        assertNull(deletedUser);
    }

    @Test
    public void testChangeStatus() {

        MyUser user = new MyUser(null, "user1", passwordEncoder.encode("password1"), "user1@example.com", "ADMIN", null, null, null);
        MyUser savedU=myUserRepository.save(user);

        companyService.changeStatus(savedU, testCompany.getId(), "Approved");

        // Verify that the company's status is updated in the database
        Company updatedCompany = companyRepository.findById(testCompany.getId()).orElse(null);
        assertNotNull(updatedCompany);
        assertEquals("Approved", updatedCompany.getStatus());
    }

    @Test
    public void testGetCompanyBranches() {
        // Perform the getCompanyBranches operation
        List<Branch> branches = companyService.getCompanyBranches(testUser);

        // Verify that the returned branches belong to the company
        for (Branch branch : branches) {
            assertEquals(testCompany, branch.getCompany());
        }
    }

    @Test
    public void testGetCompanyDetails() {
        // Perform the getCompanyDetails operation
        MyUser companyUser = companyService.getCompanyDetails(testUser);

        // Verify that the returned user is associated with the company
        assertEquals(testCompany, companyUser.getCompany());
    }

    // Write more test methods for other scenarios

}

