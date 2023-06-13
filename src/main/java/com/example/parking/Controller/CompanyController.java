package com.example.parking.Controller;


import com.example.parking.DTO.CompanyDTO;
import com.example.parking.Model.Company;
import com.example.parking.Model.MyUser;
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

    @PostMapping("add")
    public ResponseEntity registerCompany(@Valid @RequestBody CompanyDTO companyDTO){
        companyService.addCompany(companyDTO);
        return ResponseEntity.status(200).body("Company Registered");
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
}
