package com.example.parking.Service;



import com.example.parking.ApiException.ApiException;
import com.example.parking.DTO.BookingDTO;
import com.example.parking.Model.*;
import com.example.parking.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BranchRepository branchRepository;
    private final ParkingRepository parkingRepository;
    private final CustomerRepository customerRepository;
    private final CarRepository carRepository;
    private final TimeRepository timeRepository;
    private final CompanyRepository companyRepository;

/*
        get Bookings BookingDTO
 */
    public void bookingParking(MyUser user, BookingDTO bookingDTO){
        Branch branch = branchRepository.findBranchById(bookingDTO.getBranchId());
        if (branch == null){
            throw new ApiException("Branch Not Found");
        }

        Parking parking = parkingRepository.findParkingByParkingNumber(bookingDTO.getParkingNumber());
        if (parking == null){
            throw new ApiException("Parking Not Found");
        }
        List<Time> times = timeRepository.findAllByParking(parking);
        for (int i = 0; i < times.size(); i++) {
            if (times.get(i).getArrivalTime().equals(bookingDTO.getArrivalTime())){
                throw new ApiException("Parking Is Booked");
            }
            if (times.get(i).getDepartureTime().equals(bookingDTO.getDepartureTime())){
                throw new ApiException("Parking Is Booked");
            }
            if (times.get(i).getArrivalTime().isBefore(bookingDTO.getArrivalTime()) && times.get(i).getDepartureTime().isAfter(bookingDTO.getArrivalTime())){
                throw new ApiException("Parking Is Booked");
            }
        }
        Car car = carRepository.findCarById(bookingDTO.getCarId());
        if (car == null){
            throw new ApiException("Car Not Found");
        }

        if (!car.getHandicap() && parking.getHandicap()){
            throw new ApiException("Sorry Parking is only for handicap");
        }



        LocalDateTime localDateTime = LocalDateTime.now();

        if (localDateTime.isAfter(bookingDTO.getArrivalTime())){
            throw new ApiException("Cannot Booking");
        }

        Time time = new Time();
        time.setArrivalTime(bookingDTO.getArrivalTime());
        time.setDepartureTime(bookingDTO.getDepartureTime());
        time.setParking(parking);
        timeRepository.save(time);

        Integer totalHours =time.getDepartureTime().getHour() - time.getArrivalTime().getHour();
        Double totalPrice = parking.getPrice() * totalHours;
        if (parking.getHandicap()){
            totalPrice = 0.0;
        }

        Customer customer = customerRepository.findCustomerByUser(user);
        customer.setBalance(customer.getBalance() - totalPrice);

        Company company = companyRepository.findCompanyByBranchSetContains(branch);
        company.setRevenue(company.getRevenue() + totalPrice);

        Booking booking = new Booking();
        booking.setParking(parking);
        booking.setTime(time);
        booking.setCar(car);
        booking.setTotalPrice(totalPrice);
        booking.setStatus("new");

        bookingRepository.save(booking);
    }

    public void updateBookingParking(MyUser user, BookingDTO bookingDTO, Integer bookingId){

        Booking booking = bookingRepository.findBookingById(bookingId);
        Customer customer = customerRepository.findCustomerByUser(user);
        if(!(customer.getId().equals(booking.getCar().getCustomer().getId()))){
            throw new ApiException("Not Authorized");
        }

        LocalDateTime localDateTime = LocalDateTime.now();

        if (localDateTime.isAfter(booking.getTime().getArrivalTime())){
            throw new ApiException("Cannot Update Booking");
        }
        Branch branch = branchRepository.findBranchById(bookingDTO.getBranchId());
        if (branch == null){
            throw new ApiException("Branch Not Found");
        }

        Parking parking = parkingRepository.findParkingByParkingNumber(bookingDTO.getParkingNumber());
        if (parking == null){
            throw new ApiException("Parking Not Found");
        }
        List<Time> times = timeRepository.findAllByParking(parking);
        for (int i = 0; i < times.size(); i++) {
            if (times.get(i).getArrivalTime().equals(bookingDTO.getArrivalTime())){
                throw new ApiException("Parking Is Booked");
            }
            if (times.get(i).getDepartureTime().equals(bookingDTO.getDepartureTime())){
                throw new ApiException("Parking Is Booked");
            }
            if (times.get(i).getArrivalTime().isBefore(bookingDTO.getArrivalTime()) && times.get(i).getDepartureTime().isAfter(bookingDTO.getArrivalTime())){
                throw new ApiException("Parking Is Booked");
            }
        }
        Car car = carRepository.findCarById(bookingDTO.getCarId());
        if (car == null){
            throw new ApiException("Car Not Found");
        }


        if (!car.getHandicap() && parking.getHandicap()){
            throw new ApiException("Sorry Parking is only for handicap");
        }

        Time time = booking.getTime();
        time.setArrivalTime(bookingDTO.getArrivalTime());
        time.setDepartureTime(bookingDTO.getDepartureTime());
        time.setParking(parking);
        timeRepository.save(time);

        Integer totalHours =time.getDepartureTime().getHour() - time.getArrivalTime().getHour();
        Double totalPrice = parking.getPrice() * totalHours;


        Company company = companyRepository.findCompanyByBranchSetContains(branch);

        if (parking.getHandicap()){
            totalPrice = 0.0;
        }

        if (totalPrice > booking.getTotalPrice()){
            Double sum = totalPrice - booking.getTotalPrice();
            customer.setBalance(customer.getBalance() - sum);

            company.setRevenue(company.getRevenue() + sum);
        }
        if (totalPrice < booking.getTotalPrice()){
            Double sum = booking.getTotalPrice() - totalPrice;
            customer.setBalance(customer.getBalance() + sum);

            company.setRevenue(company.getRevenue() - sum);
        }

        booking.setParking(parking);
        booking.setTime(time);
        booking.setCar(car);
        booking.setTotalPrice(totalPrice);

        bookingRepository.save(booking);
    }

    public void cancelBookingParking(MyUser user,Integer bookingId){

        Booking booking = bookingRepository.findBookingById(bookingId);
        Customer customer = customerRepository.findCustomerByUser(user);
        if(!(customer.getId().equals(booking.getCar().getCustomer().getId()))){
            throw new ApiException("Not Authorized");
        }

        LocalDateTime localDateTime = LocalDateTime.now();
        if (localDateTime.isAfter(booking.getTime().getArrivalTime())){
            throw new ApiException("Cannot Cancel Booking");
        }

        Double price = booking.getTotalPrice();

        customer.setBalance(customer.getBalance() + price);

        Company company = companyRepository.findCompanyByBranchSetContains(booking.getParking().getBranch());

        company.setRevenue(company.getRevenue() - price);
        Time time = booking.getTime();
        time.setParking(null);

        booking.setTime(null);
        timeRepository.delete(time);
        bookingRepository.delete(booking);

    }

    public void checkOut(MyUser user,Integer bookingId){
        Booking booking = bookingRepository.findBookingById(bookingId);
        Customer customer = customerRepository.findCustomerByUser(user);
        if(!(customer.getId().equals(booking.getCar().getCustomer().getId()))){
            throw new ApiException("Not Authorized");
        }

        booking.setStatus("expired");
        bookingRepository.save(booking);
    }

//    public void






}
