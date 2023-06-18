package com.example.parking.Controller;

import com.example.parking.Controller.CompanyController;
import com.example.parking.DTO.CompanyDTO;
import com.example.parking.Model.Branch;
import com.example.parking.Model.Company;
import com.example.parking.Model.MyUser;
import com.example.parking.Service.CompanyService;
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

class CompanyControllerTest {

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CompanyController companyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCompanies_shouldReturnListOfCompanies() {
        // Arrange
        List<Company> companies = new ArrayList<>();
        companies.add(new Company());
        companies.add(new Company());
        Mockito.when(companyService.getCompanies()).thenReturn(companies);

        // Act
        ResponseEntity<List<Company>> response = companyController.getCompanies();

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(companies, response.getBody());
        Mockito.verify(companyService, Mockito.times(1)).getCompanies();
    }

    @Test
    void updateCompany_shouldReturnCompanyUpdatedMessage() {
        // Arrange
        MyUser user = new MyUser();
        CompanyDTO companyDTO = new CompanyDTO("stc","stc@123","mohammed@gmail.com","stc","stopped",2.4);
        Integer companyId = 1;

        // Act
        ResponseEntity<String> response = companyController.updateCompany(user, companyDTO, companyId);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Company Updated", response.getBody());
        Mockito.verify(companyService, Mockito.times(1)).updateCompany(user, companyDTO, companyId);
    }

    @Test
    void deleteCompany_shouldReturnCompanyDeletedMessage() {
        // Arrange
        MyUser user = new MyUser();
        Integer companyId = 1;

        // Act
        ResponseEntity<String> response = companyController.deleteCompany(user, companyId);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Company Deleted", response.getBody());
        Mockito.verify(companyService, Mockito.times(1)).deleteCompany(user, companyId);
    }

    @Test
    void changeStatus_shouldReturnCompanyStatusUpdatedMessage() {
        // Arrange
        MyUser user = new MyUser();
        String status = "Active";
        Integer companyId = 1;

        // Act
        ResponseEntity<String> response = companyController.changeStatus(user, status, companyId);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Company Status Updated to Active", response.getBody());
        Mockito.verify(companyService, Mockito.times(1)).changeStatus(user, companyId, status);
    }

    @Test
    void getCompanyBranches_shouldReturnListOfBranches() {
        // Arrange
        MyUser user = new MyUser();
        List<Branch> branches = new ArrayList<>();
        branches.add(new Branch());
        branches.add(new Branch());
        Mockito.when(companyService.getCompanyBranches(user)).thenReturn(branches);

        // Act
        ResponseEntity<List<Branch>> response = companyController.getCompanyBranches(user);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(branches, response.getBody());
        Mockito.verify(companyService, Mockito.times(1)).getCompanyBranches(user);
    }

    @Test
    void getCompanyDetails_shouldReturnUserDetails() {
        // Arrange
        MyUser user = new MyUser();
        Mockito.when(companyService.getCompanyDetails(user)).thenReturn(user);

        // Act
        ResponseEntity<MyUser> response = companyController.getCompanyDetails(user);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(user, response.getBody());
        Mockito.verify(companyService, Mockito.times(1)).getCompanyDetails(user);
    }
}

