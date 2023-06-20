package com.example.parking.Service;

import com.example.parking.Repository.*;
import com.example.parking.Service.BranchService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import com.example.parking.ApiException.ApiException;
import com.example.parking.Model.Branch;
import com.example.parking.Model.Company;
import com.example.parking.Model.MyUser;
import com.example.parking.Model.Parking;
import com.example.parking.Repository.BranchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class BranchServiceTest {
    @Autowired
    private  BranchRepository branchRepository;
    @Autowired
    private  CompanyRepository companyRepository;
    @Autowired
    private  ParkingRepository parkingRepository;
    @Autowired
    private  BookingRepository bookingRepository;
    private  BranchService branchService;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private  MyUserRepository myUserRepository;

    @BeforeEach
    void setUp() {
        branchService=new BranchService(branchRepository,companyRepository,parkingRepository,bookingRepository);
    }

//    @Test
//    void getBranches_shouldReturnListOfBranches() {
//        // Arrange
//        Branch branch1 = Branch.builder().location("gsdsd").name("sgsdgsdg").phoneNum("sgsdgdsg").build();
//        Branch savedB=branchRepository.save(branch1);
//
//        Branch branch2 = Branch.builder().location("gsdsd").name("sgsdgsdg").phoneNum("sgsdgdsg").build();
//        Branch savedB1=branchRepository.save(branch2);
//
//        // Act
//        List<Branch> result = branchService.getBranches();
//
//        // Assert
//        assertEquals(2, result.size());
//        assertTrue(result.contains(branch1));
//        assertTrue(result.contains(branch2));
//    }

    @Test
    void addBranch_shouldAddBranchForCompany() {
        // Arrange
        MyUser user = new MyUser(null, "user1", passwordEncoder.encode("password1"), "user1@example.com", "COMPANY", null, null);
        MyUser savedU=myUserRepository.save(user);
        Company company = Company.builder().revenue(365343.544).status("approved").name("sdgsdg").user(savedU).build();
        Company savedC=companyRepository.save(company);
        Branch branch = Branch.builder().location("gsdsd").name("sgsdgsdg").phoneNum("sgsdgdsg").company(savedC).build();

        Branch savedB=branchRepository.save(branch);

        // Act
        assertDoesNotThrow(() -> branchService.addBranch(savedU, savedB));

        // Assert
        List<Branch> companyBranches = branchRepository.findBranchesByCompany(savedC);
        assertEquals(1, companyBranches.size());
        assertEquals(branch, companyBranches.get(0));
    }

    @Test
    void addBranch_shouldThrowExceptionIfUserIsNotCompany() {
        // Arrange
        MyUser savedU= new MyUser(null, "user1", passwordEncoder.encode("password1"), "user1@example.com", "COMPANY", null, null);
        MyUser user=myUserRepository.save(savedU);
        Company savedC = Company.builder().revenue(365341.45).status("pending").name("sdgsdg").user(savedU).build();
        Company company=companyRepository.save(savedC);
        Branch branchT = Branch.builder().location("gsdsd").name("sgsdgsdg").phoneNum("sgsdgdsg").company(company).build();

        Branch branch=branchRepository.save(branchT);

        // Act & Assert
        assertThrows(ApiException.class, () -> branchService.addBranch(user, branch));
    }

    @Test
    void updateBranch_shouldUpdateBranchForCompany() {
        // Arrange
        MyUser savedU= new MyUser(null, "user1", passwordEncoder.encode("password1"), "user1@example.com", "COMPANY", null, null);
        MyUser user=myUserRepository.save(savedU);
        Company savedC = Company.builder().revenue(365341.45).status("pending").name("sdgsdg").user(savedU).build();
        Company company=companyRepository.save(savedC);
        Branch branchT = Branch.builder().location("gsdsd").name("sgsdgsdg").phoneNum("sgsdgdsg").company(company).build();

        Branch branch=branchRepository.save(branchT);

        branch.setName("Updated Branch");

        // Act
        assertDoesNotThrow(() -> branchService.updateBranch(user, branch, branch.getId()));

        // Assert
        Optional<Branch> updatedBranch = branchRepository.findById(branch.getId());
        assertTrue(updatedBranch.isPresent());
        assertEquals("Updated Branch", updatedBranch.get().getName());
    }

    @Test
    void updateBranch_shouldThrowExceptionIfUserIsNotCompany() {
        // Arrange
        MyUser savedU= new MyUser(null, "user1", passwordEncoder.encode("password1"), "user1@example.com", "COMPANY", null, null);
        MyUser user=myUserRepository.save(savedU);
        Company savedC = Company.builder().revenue(365341.45).status("pending").name("sdgsdg").user(savedU).build();
        Company company=companyRepository.save(savedC);
        Branch branchT = Branch.builder().location("gsdsd").name("sgsdgsdg").phoneNum("sgsdgdsg").company(company).build();

        Branch branch=branchRepository.save(branchT);
        // Act & Assert
        assertThrows(ApiException.class, () -> branchService.updateBranch(user, branch, 1));
    }

    @Test
    void updateBranch_shouldThrowExceptionIfBranchNotFound() {
        // Arrange
        MyUser savedU= new MyUser(null, "user1", passwordEncoder.encode("password1"), "user1@example.com", "COMPANY", null, null);
        MyUser user=myUserRepository.save(savedU);
        Company savedC = Company.builder().revenue(365341.45).status("pending").name("sdgsdg").user(savedU).build();
        Company company=companyRepository.save(savedC);
        Branch branchT = Branch.builder().location("gsdsd").name("sgsdgsdg").phoneNum("sgsdgdsg").company(company).build();

        Branch branch=branchRepository.save(branchT);


        // Act & Assert
        assertThrows(ApiException.class, () -> branchService.updateBranch(user, branch, 1));
    }

    @Test
    void deleteBranch_shouldDeleteBranchForCompany() {
        // Arrange
        MyUser savedU= new MyUser(null, "user1", passwordEncoder.encode("password1"), "user1@example.com", "COMPANY", null, null);
        MyUser user=myUserRepository.save(savedU);
        Company savedC = Company.builder().revenue(365341.45).status("pending").name("sdgsdg").user(savedU).build();
        Company company=companyRepository.save(savedC);
        Branch branchT = Branch.builder().location("gsdsd").name("sgsdgsdg").phoneNum("sgsdgdsg").company(company).build();

        Branch branch=branchRepository.save(branchT);
        // Act
        assertDoesNotThrow(() -> branchService.deleteBranch(company.getUser(), branch.getId()));

        // Assert
        Optional<Branch> deletedBranch = branchRepository.findById(branch.getId());
        assertFalse(deletedBranch.isPresent());
    }

    @Test
    void deleteBranch_shouldThrowExceptionIfUserIsNotCompany() {
        // Arrange
        MyUser savedU= new MyUser(null, "user1", passwordEncoder.encode("password1"), "user1@example.com", "COMPANY", null, null);
        MyUser user=myUserRepository.save(savedU);
        Company savedC = Company.builder().revenue(365341.45).status("pending").name("sdgsdg").user(savedU).build();
        Company company=companyRepository.save(savedC);
        Branch branchT = Branch.builder().location("gsdsd").name("sgsdgsdg").phoneNum("sgsdgdsg").company(company).build();

        Branch branch=branchRepository.save(branchT);
        // Act & Assert
        assertThrows(ApiException.class, () -> branchService.deleteBranch(user, 1));
    }

    @Test
    void deleteBranch_shouldThrowExceptionIfBranchNotFound() {
        // Arrange
        MyUser savedU= new MyUser(null, "user1", passwordEncoder.encode("password1"), "user1@example.com", "COMPANY", null, null);
        MyUser user=myUserRepository.save(savedU);
        Company savedC = Company.builder().revenue(365341.45).status("pending").name("sdgsdg").user(savedU).build();
        Company company=companyRepository.save(savedC);
        Branch branchT = Branch.builder().location("gsdsd").name("sgsdgsdg").phoneNum("sgsdgdsg").company(company).build();

        Branch branch=branchRepository.save(branchT);
        // Act & Assert
        assertThrows(ApiException.class, () -> branchService.deleteBranch(user, 1));
    }

//    @Test
//    void getParkingByBranch_shouldReturnListOfParkingForBranch() {
//        // Arrange
//        Branch branch = new Branch();
//        Parking parking1 = new Parking();
//        Parking parking2 = new Parking();
//
//        branchRepository.save(branch);
//        parking1.setBranch(branch);
//        parking2.setBranch(branch);
//        parkingRepository.save(parking1);
//        parkingRepository.save(parking2);
//
//        // Act
//        List<Parking> result = branchService.getParkingByBranch(branch.getId());
//
//        // Assert
//        assertEquals(2, result.size());
//        assertTrue(result.contains(parking1));
//        assertTrue(result.contains(parking2));
//    }
//
//    @Test
//    void getParkingByBranch_shouldThrowExceptionIfBranchNotFound() {
//        // Arrange
//        // Non-existing branch ID
//        Integer branchId = 1;
//
//        // Act & Assert
//        assertThrows(ApiException.class, () -> branchService.getParkingByBranch(branchId));
//    }
}

