package com.example.parking.Controller;


import com.example.parking.DTO.CompanyDTO;
import com.example.parking.Model.*;
import com.example.parking.Service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/get")
    public ResponseEntity getCompanies(){
        List<Company> companies = companyService.getCompanies();
        return ResponseEntity.status(200).body(companies);
    }


    @PutMapping("update/{companyId}")
    public ResponseEntity updateCompany(@AuthenticationPrincipal MyUser user, @RequestBody CompanyDTO companyDTO, @PathVariable Integer companyId){
        companyService.updateCompany(user, companyDTO, companyId);
        return ResponseEntity.status(200).body("Company Updated");
    }

    @DeleteMapping("delete/{companyId}")
    public ResponseEntity deleteCompany(@AuthenticationPrincipal MyUser user, @PathVariable Integer companyId){
        companyService.deleteCompany(user,companyId);
        return ResponseEntity.status(200).body("Company Deleted");
    }

    @PutMapping("update-status/{companyId}")
    public ResponseEntity changeStatus(@AuthenticationPrincipal MyUser user, @RequestBody String status, @PathVariable Integer companyId){
        companyService.changeStatus(user, companyId,status);
        return ResponseEntity.status(200).body("Company Status Updated");
    }

    @GetMapping("/get-branch")
    public ResponseEntity getCompanyBranches(@AuthenticationPrincipal MyUser user){
        List<Branch> branches= companyService.getCompanyBranches(user);
        return ResponseEntity.status(200).body(branches);
    }

    @GetMapping("/details")
    public ResponseEntity getCompanyDetails(@AuthenticationPrincipal MyUser user){
        MyUser myUser =companyService.getCompanyDetails(user);
        return ResponseEntity.status(200).body(myUser);
    }


}
