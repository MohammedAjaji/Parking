package com.example.parking.Service;

import com.example.parking.ApiException.ApiException;
import com.example.parking.DTO.BookingDTO;
import com.example.parking.Model.*;
import com.example.parking.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ParkingService {

    private final ParkingRepository parkingRepository;
    private final BranchRepository branchRepository;
    private final CompanyRepository companyRepository;
    private final TimeRepository timeRepository;
    private final BookingRepository bookingRepository;


    public List<Parking> getParking() {
        return parkingRepository.findAll();
    }

    public void addParking(MyUser user, Integer branchId, Parking parking) {
        Company company = companyRepository.findCompanyByUser(user);
        if (company == null){
            throw new ApiException("Sorry Only Companies can add parking to  Branch");
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
            throw new ApiException("Sorry Only Companies can update Parking");
        }

        Branch branch = branchRepository.findBranchById(branchId);
        if (branch == null){
            throw new ApiException("Branch Not Found");
        }

        if (!Objects.equals(branch.getCompany().getUser().getId(), company.getUser().getId())){
            throw new ApiException("Not Authorized");
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
            throw new ApiException("Sorry Only Companies can delete Parking");
        }

        Branch branch = branchRepository.findBranchById(branchId);
        if (branch == null){
            throw new ApiException("Branch Not Found");
        }

        if (!Objects.equals(branch.getCompany().getUser().getId(), company.getUser().getId())){
            throw new ApiException("Not Authorized");
        }

        Parking parking = parkingRepository.findParkingById(parkingId);
        if (parking == null){
            throw new ApiException("Parking Not found");
        }
        List<Booking> bookings = bookingRepository.findAllByParking(parking);
        for (int k = 0; k < bookings.size(); k++) {
            if (bookings.get(k).getStatus().equalsIgnoreCase("new") || bookings.get(k).getStatus().equalsIgnoreCase("active")){
                throw new ApiException("Cannot Delete parking where there are Bookings");
            }
            bookings.get(k).setParking(null);

        }

        parkingRepository.delete(parking);

    }

    public List<Parking> getParkingByTime(BookingDTO bookingDTO, Integer branchId){
        Branch branch = branchRepository.findBranchById(branchId);
        if (branch == null){
            throw new ApiException("Branch not found");
        }
//        Time time = timeRepository.findTimeByArrivalTimeAndDepartureTime(bookingDTO.getArrivalTime(), bookingDTO.getDepartureTime());
//        if (time == null){
//            throw new ApiException("time not found");
//        }
        List<Time> times = timeRepository.findAvailableTimes(bookingDTO.getArrivalTime(), bookingDTO.getDepartureTime());
        List<Parking> parking = new ArrayList<>();
        for (int i = 0; i < times.size(); i++) {
            if (times.get(i).getParking().getBranch().getId().equals(branchId)){
                parking.add(times.get(i).getParking());
            }

        }
        return parking;
    }


    public Integer getNotAvailableParking(BookingDTO bookingDTO, Integer branchId){
        Branch branch = branchRepository.findBranchById(branchId);
        if (branch == null){
            throw new ApiException("Branch not found");
        }
        List<Time> times = timeRepository.findAvailableTimes(bookingDTO.getArrivalTime(), bookingDTO.getDepartureTime());
        List<Parking> parking = new ArrayList<>();

        for (int i = 0; i < times.size(); i++) {
            if (times.get(i).getParking().getBranch().getId().equals(branchId)){
                parking.add(times.get(i).getParking());
            }
        }
        return parking.size();
    }

    public Integer getAvailableParking(BookingDTO bookingDTO, Integer branchId){
        Branch branch = branchRepository.findBranchById(branchId);
        if (branch == null){
            throw new ApiException("Branch not found");
        }
        List<Time> times = timeRepository.findAvailableTimes(bookingDTO.getArrivalTime(), bookingDTO.getDepartureTime());
        List<Parking> parkingList = parkingRepository.findAllByBranch(branch);
        List<Parking> parking = new ArrayList<>();

        for (int i = 0; i < times.size(); i++) {
            if (times.get(i).getParking().getBranch().getId().equals(branchId)){
                parking.add(times.get(i).getParking());
            }
        }

        Integer sum = Math.abs(parking.size() - parkingList.size());

        return sum;
    }
}