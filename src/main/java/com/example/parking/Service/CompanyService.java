package com.example.parking.Service;

import com.example.parking.ApiException.ApiException;
import com.example.parking.DTO.CompanyDTO;
import com.example.parking.Model.Company;
import com.example.parking.Model.MyUser;
import com.example.parking.Repository.CompanyRepository;
import com.example.parking.Repository.MyUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final MyUserRepository myUserRepository;

    public List<Company> getCompanies(){
        return companyRepository.findAll();
    }

    public void addCompany(CompanyDTO companyDTO){
        String hash = new BCryptPasswordEncoder().encode(companyDTO.getPassword());

        MyUser user = new MyUser();
        user.setUsername(companyDTO.getUsername());
        user.setPassword(hash);
        user.setEmail(companyDTO.getEmail());
        user.setRole("COMPANY");

        Company company = new Company();
        company.setName(companyDTO.getName());
        company.setRevenue(0.0);
        company.setStatus("Pending");

        user.setCompany(company);

        myUserRepository.save(user);
        companyRepository.save(company);
    }

    public void updateCompany(MyUser user, CompanyDTO companyDTO, Integer companyId){
       Company company = companyRepository.findCompanyById(companyId);

       if (!Objects.equals(user.getCompany().getId(), companyId)){
           throw new ApiException("Not Authorized");
       }

       if (company == null){
           throw new ApiException("Company Not found");
       }

       company.setName(companyDTO.getName());
       company.setRevenue(companyDTO.getRevenue());

       companyRepository.save(company);

    }

    public void deleteCompany(MyUser user, Integer companyId){
        Company company = companyRepository.findCompanyById(companyId);

        if (!Objects.equals(user.getCompany().getId(), companyId)){
            throw new ApiException("Not Authorized");
        }

        if (company == null){
            throw new ApiException("Company Not found");
        }

        companyRepository.delete(company);
    }
}
