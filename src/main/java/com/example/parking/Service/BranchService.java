package com.example.parking.Service;

import com.example.parking.ApiException.ApiException;
import com.example.parking.Model.Branch;
import com.example.parking.Model.Company;
import com.example.parking.Model.MyUser;
import com.example.parking.Model.Parking;
import com.example.parking.Repository.CompanyRepository;
import com.example.parking.Repository.BranchRepository;
import com.example.parking.Repository.ParkingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final CompanyRepository companyRepository;
    private final ParkingRepository parkingRepository;

    public List<Branch> getBranches() {
        return branchRepository.findAll();
    }

    public void addBranch(MyUser user, Branch branch) {
        Company company = companyRepository.findCompanyByUser(user);
        if (company == null){
            throw new ApiException("Sorry Only Companies can add Branch");
        }
        if (!(company.getStatus().equalsIgnoreCase("approved"))){
            throw new ApiException("Company not been approved");
        }

        branch.setCompany(company);
        branchRepository.save(branch);

    }

    public void updateBranch(MyUser user, Branch branch, Integer branchId) {
        Company company = companyRepository.findCompanyByUser(user);
        if (company == null){
            throw new ApiException("Sorry Only Companies can update Branch");
        }

        Branch oldBranch = branchRepository.findBranchById(branchId);
        if (oldBranch == null){
            throw new ApiException("Branch Not Found");
        }

        if (!Objects.equals(branch.getCompany().getId(), company.getId())){
            throw new ApiException("Not Authorized");
        }

        oldBranch.setName(branch.getName());
        oldBranch.setLocation(branch.getLocation());
        oldBranch.setPhoneNum(branch.getPhoneNum());

        branchRepository.save(oldBranch);

    }

    public void deleteBranch(MyUser user, Integer branchId) {
        Company company = user.getCompany();
        if (company == null){
            throw new ApiException("Sorry Only Companies can delete Branch");
        }

        Branch branch = branchRepository.findBranchById(branchId);
        if (branch == null){
            throw new ApiException("Branch Not Found");
        }
        if (!Objects.equals(branch.getCompany().getId(), company.getId())){
            throw new ApiException("Not Authorized");
        }

        branchRepository.delete(branch);
    }

    public List<Parking> getParkingByBranch(Integer branchId){
        Branch branch = branchRepository.findBranchById(branchId);
        if (branch == null){
            throw new ApiException("Branch Not Found");
        }

        List<Parking> parking = parkingRepository.findAllByBranch(branch);

        return parking;

    }
}
