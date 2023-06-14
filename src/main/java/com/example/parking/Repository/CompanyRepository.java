package com.example.parking.Repository;

import com.example.parking.Model.Company;
import com.example.parking.Model.Customer;
import com.example.parking.Model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    Company findCompanyById(Integer id);

    Company findCompanyByUser(MyUser user);

}
