package com.example.parking.Service;

import com.example.parking.ApiException.ApiException;
import com.example.parking.DTO.CompanyDTO;
import com.example.parking.Model.*;
import com.example.parking.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final MyUserRepository myUserRepository;
    private final BranchRepository branchRepository;
    private final ParkingRepository parkingRepository;
    private final BookingRepository bookingRepository;

    public List<Company> getCompanies(){
        return companyRepository.findAll();
    }

    public Company addCompany(CompanyDTO companyDTO){
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
        company.setUser(user);

        myUserRepository.save(user);
        companyRepository.save(company);
        return companyRepository.save(company);
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

        for (int i = 0; i < branches.size(); i++) {
            List<Parking> parking = parkingRepository.findAllByBranch(branches.get(i));
            for (int j = 0; j < parking.size(); j++) {
                List<Booking> bookings = bookingRepository.findAllByParking(parking.get(j));
                for (int k = 0; k < bookings.size(); k++) {
                    if (bookings.get(k).getStatus().equalsIgnoreCase("new") || bookings.get(k).getStatus().equalsIgnoreCase("active")){
                        throw new ApiException("Cannot Delete Company where there are Bookings");
                    }
                    bookings.get(k).setParking(null);
                }
                parking.get(j).setBranch(null);
                parkingRepository.delete(parking.get(j));

            }
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

    public List<Branch> getCompanyBranches(MyUser user){
        Company company = companyRepository.findCompanyByUser(user);
        if (company == null) {
            throw new ApiException("Not Authorized");
        }
        List<Branch> branches = branchRepository.findBranchesByCompany(company);
        return branches;
    }

    //endpoint that takes company id and return All company details
    public MyUser getCompanyDetails(MyUser user){
        Company company= companyRepository.findCompanyByUser(user);
        if(company==null){
            throw new ApiException("company not found");
        }
        return user;
    }
}
