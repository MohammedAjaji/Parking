package com.example.parking.Controller;


import com.example.parking.Model.Branch;
import com.example.parking.Model.MyUser;
import com.example.parking.Model.Parking;
import com.example.parking.Service.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/branch")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @GetMapping("/get")
    public ResponseEntity getBranches(){
        List<Branch> branches = branchService.getBranches();
        return ResponseEntity.status(200).body(branches);
    }

    @PostMapping("/add")
    public ResponseEntity addBranch(@AuthenticationPrincipal MyUser user,@Valid @RequestBody Branch branch){
        branchService.addBranch(user, branch);
        return ResponseEntity.status(200).body("Branch Added");
    }

    @PutMapping("/update/{branchId}")
    public ResponseEntity updateBranch(@AuthenticationPrincipal MyUser user, @Valid @RequestBody Branch branch, @PathVariable Integer branchId){
        branchService.updateBranch(user, branch, branchId);
        return ResponseEntity.status(200).body("Branch Updated");
    }

    @DeleteMapping("/delete/{branchId}")
    public ResponseEntity deleteBranch(@AuthenticationPrincipal MyUser user, @PathVariable Integer branchId){
        branchService.deleteBranch(user,branchId);
        return ResponseEntity.status(200).body("Branch Deleted");
    }

    @GetMapping("/get-parking/{branchId}")
    public ResponseEntity getParkingByBranch(@PathVariable Integer branchId){
        List<Parking> parking = branchService.getParkingByBranch(branchId);
        return ResponseEntity.status(200).body(parking);
    }
}
