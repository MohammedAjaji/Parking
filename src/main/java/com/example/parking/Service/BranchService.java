package com.example.parking.Service;

import com.example.parking.ApiException.ApiException;
import com.example.parking.Model.Branch;
import com.example.parking.Model.Company;
import com.example.parking.Model.MyUser;
import com.example.parking.Repository.CompanyRepository;
import com.example.parking.Repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final CompanyRepository companyRepository;

    public List<Branch> getBranches() {
        return branchRepository.findAll();
    }

    public void addBranch(MyUser user, Branch branch) {
        Company company = companyRepository.findCompanyByUser(user);
        if (company == null){
            throw new ApiException("Sorry Only Companies can add Branch");
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

        branchRepository.delete(branch);
    }
}
