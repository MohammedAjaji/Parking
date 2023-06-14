package com.example.parking.Repository;

import com.example.parking.Model.Branch;
import com.example.parking.Model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {

    Branch findBranchById(Integer id);

    List<Branch> findBranchesByCompany(Company company);
}
