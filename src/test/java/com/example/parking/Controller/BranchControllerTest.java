package com.example.parking.Controller;

import com.example.parking.Controller.BranchController;
import com.example.parking.Model.Branch;
import com.example.parking.Model.MyUser;
import com.example.parking.Model.Parking;
import com.example.parking.Service.BranchService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import org.mockito.Mockito;


class BranchControllerTest {

    @Mock
    private BranchService branchService;

    @InjectMocks
    private BranchController branchController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getBranches_shouldReturnListOfBranches() {
        // Arrange
        List<Branch> branches = new ArrayList<>();
        branches.add(new Branch());
        branches.add(new Branch());
        Mockito.when(branchService.getBranches()).thenReturn(branches);

        // Act
        ResponseEntity<List<Branch>> response = branchController.getBranches();

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(branches, response.getBody());
        Mockito.verify(branchService, Mockito.times(1)).getBranches();
    }

    @Test
    void addBranch_shouldReturnBranchAddedMessage() {
        // Arrange
        MyUser user = new MyUser();
        Branch branch = new Branch();

        // Act
        ResponseEntity<String> response = branchController.addBranch(user, branch);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Branch Added", response.getBody());
        Mockito.verify(branchService, Mockito.times(1)).addBranch(user, branch);
    }

    @Test
    void updateBranch_shouldReturnBranchUpdatedMessage() {
        // Arrange
        MyUser user = new MyUser();
        Branch branch = new Branch();
        Integer branchId = 1;

        // Act
        ResponseEntity<String> response = branchController.updateBranch(user, branch, branchId);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Branch Updated", response.getBody());
        Mockito.verify(branchService, Mockito.times(1)).updateBranch(user, branch, branchId);
    }

    @Test
    void deleteBranch_shouldReturnBranchDeletedMessage() {
        // Arrange
        MyUser user = new MyUser();
        Integer branchId = 1;

        // Act
        ResponseEntity<String> response = branchController.deleteBranch(user, branchId);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Branch Deleted", response.getBody());
        Mockito.verify(branchService, Mockito.times(1)).deleteBranch(user, branchId);
    }

    @Test
    void getParkingByBranch_shouldReturnListOfParking() {
        // Arrange
        Integer branchId = 1;
        List<Parking> parking = new ArrayList<>();
        parking.add(new Parking());
        parking.add(new Parking());
        Mockito.when(branchService.getParkingByBranch(branchId)).thenReturn(parking);

        // Act
        ResponseEntity<List<Parking>> response = branchController.getParkingByBranch(branchId);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(parking, response.getBody());
        Mockito.verify(branchService, Mockito.times(1)).getParkingByBranch(branchId);
    }
}

