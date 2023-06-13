package com.example.parking.Service;

import com.example.parking.ApiException.ApiException;
import com.example.parking.Model.Branch;
import com.example.parking.Model.Company;
import com.example.parking.Model.MyUser;
import com.example.parking.Model.Parking;
import com.example.parking.Repository.CompanyRepository;
import com.example.parking.Repository.ParkingRepository;
import com.example.parking.Repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingService {

    private final ParkingRepository parkingRepository;
    private final BranchRepository branchRepository;
    private final CompanyRepository companyRepository;


    public List<Parking> getParking() {
        return parkingRepository.findAll();
    }

    public void addParking(MyUser user, Integer branchId, Parking parking) {
        Company company = companyRepository.findCompanyByUser(user);
        if (company == null){
            throw new ApiException("Sorry Only Companies can add Branch");
        }

        Branch branch =branchRepository.findBranchById(branchId);
        if (branch == null){
            throw new ApiException("Branch Not Found");
        }

        parking.setBranch(branch);
        parkingRepository.save(parking);


    }

    public void updateParking(MyUser user, Parking parking, Integer branchId, Integer parkingId) {
        Company company = companyRepository.findCompanyByUser(user);
        if (company == null){
            throw new ApiException("Sorry Only Companies can add Branch");
        }

        Branch branch = branchRepository.findBranchById(branchId);
        if (branch == null){
            throw new ApiException("Branch Not Found");
        }

        Parking oldParking = parkingRepository.findParkingById(parkingId);
        if (oldParking == null){
            throw new ApiException("Parking Not found");
        }

        oldParking.setParkingNumber(parking.getParkingNumber());
        oldParking.setPrice(parking.getPrice());
        oldParking.setHandicap(parking.getHandicap());
        oldParking.setOutdoor(parking.getOutdoor());
        if (parking.getOutdoor()){
            oldParking.setFloor(0);
        }else {
            oldParking.setFloor(parking.getFloor());
        }

        parkingRepository.save(oldParking);

    }

    public void deleteParking(MyUser user, Integer branchId, Integer parkingId) {
        Company company = companyRepository.findCompanyByUser(user);
        if (company == null){
            throw new ApiException("Sorry Only Companies can delete Branch");
        }

        Branch branch = branchRepository.findBranchById(branchId);
        if (branch == null){
            throw new ApiException("Branch Not Found");
        }

        Parking parking = parkingRepository.findParkingById(parkingId);
        if (parking == null){
            throw new ApiException("Parking Not found");
        }

        parkingRepository.delete(parking);

    }
}
