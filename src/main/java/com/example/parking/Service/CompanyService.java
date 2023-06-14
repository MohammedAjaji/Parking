package com.example.parking.Service;

import com.example.parking.ApiException.ApiException;
import com.example.parking.DTO.CompanyDTO;
import com.example.parking.Model.Branch;
import com.example.parking.Model.Company;
import com.example.parking.Model.MyUser;
import com.example.parking.Model.Parking;
import com.example.parking.Repository.BranchRepository;
import com.example.parking.Repository.CompanyRepository;
import com.example.parking.Repository.MyUserRepository;
import com.example.parking.Repository.ParkingRepository;
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
    private final BranchRepository branchRepository;
    private final ParkingRepository parkingRepository;

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
        List<Branch> branches = branchRepository.findBranchesByCompany(company);
//        List<Parking> parkings =
        for (int i = 0; i < branches.size(); i++) {
            branches.get(i).setCompany(null);
            branchRepository.delete(branches.get(i));
        }

        companyRepository.delete(company);
        myUserRepository.delete(user);
    }

    public void changeStatus(MyUser user, Integer companyId, String status){

        if (!(user.getRole().equals("ADMIN"))){
            throw new ApiException("Not Authorized");
        }

        Company company = companyRepository.findCompanyById(companyId);
        if (company == null){
            throw new ApiException("Company Not found");
        }

        company.setStatus(status);
        companyRepository.save(company);


    }
}
