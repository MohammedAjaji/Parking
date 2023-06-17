package com.example.parking.Service;



import com.example.parking.ApiException.ApiException;
import com.example.parking.DTO.BookingDTO;
import com.example.parking.Model.*;
import com.example.parking.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
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
        checkAvailableParking(parking,bookingDTO);
//        List<Time> times = timeRepository.findAllByParking(parking);
//        for (int i = 0; i < times.size(); i++) {
//            if (times.get(i).getArrivalTime().equals(bookingDTO.getArrivalTime())){
//                throw new ApiException("Parking Is Booked");
//            }
//            if (times.get(i).getDepartureTime().equals(bookingDTO.getDepartureTime())){
//                throw new ApiException("Parking Is Booked");
//            }
//            if (times.get(i).getArrivalTime().isBefore(bookingDTO.getArrivalTime()) || times.get(i).getDepartureTime().isAfter(bookingDTO.getArrivalTime())){
//                throw new ApiException("Parking Is Booked");
//            }
//            if (times.get(i).getArrivalTime().isBefore(bookingDTO.getDepartureTime()) || times.get(i).getDepartureTime().isAfter(bookingDTO.getDepartureTime())){
//                throw new ApiException("Parking Is Booked");
//            }
//        }
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


        Integer totalHours =bookingDTO.getDepartureTime().getHour() - bookingDTO.getArrivalTime().getHour();
        Double totalPrice = parking.getPrice() * totalHours;

        if (parking.getHandicap()){
            totalPrice = 0.0;
            totalHours = 0;
        }

//        Integer points = totalHours * 2;

        Customer customer = customerRepository.findCustomerByUser(user);
        if (customer.getBalance() < totalPrice){
            throw new ApiException("Sorry cannot Book a parking balance is not enough");
        }
        customer.setBalance(customer.getBalance() - totalPrice);

        Company company = companyRepository.findCompanyByBranchSetContains(branch);
        company.setRevenue(company.getRevenue() + totalPrice);

        Booking booking = new Booking();
        booking.setParking(parking);
        booking.setCar(car);
        booking.setTotalPrice(totalPrice);
        booking.setStatus("new");

        Time time = new Time();
        time.setArrivalTime(bookingDTO.getArrivalTime());
        time.setDepartureTime(bookingDTO.getDepartureTime());
        time.setParking(parking);

        booking.setTime(time);
        time.setBooking(booking);

        timeRepository.save(time);
        bookingRepository.save(booking);
    }

    public void updateBookingParking(MyUser user, BookingDTO bookingDTO, Integer bookingId){
        Booking booking = bookingRepository.findBookingById(bookingId);
        if (booking == null){
            throw new ApiException("Booking not found");
        }
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

        checkAvailableParking(parking,bookingDTO);
//        List<Time> times = timeRepository.findAllByParking(parking);
//        for (int i = 0; i < times.size(); i++) {
//            if (times.get(i).getArrivalTime().equals(bookingDTO.getArrivalTime())){
//                throw new ApiException("Parking Is Booked");
//            }
//            if (times.get(i).getDepartureTime().equals(bookingDTO.getDepartureTime())){
//                throw new ApiException("Parking Is Booked");
//            }
//            if (times.get(i).getArrivalTime().isBefore(bookingDTO.getArrivalTime()) && times.get(i).getDepartureTime().isAfter(bookingDTO.getArrivalTime())){
//                throw new ApiException("Parking Is Booked");
//            }
//            if (times.get(i).getArrivalTime().isBefore(bookingDTO.getDepartureTime()) || times.get(i).getDepartureTime().isAfter(bookingDTO.getDepartureTime())){
//                throw new ApiException("Parking Is Booked");
//            }
//        }
        Car car = carRepository.findCarById(bookingDTO.getCarId());
        if (car == null){
            throw new ApiException("Car Not Found");
        }


        if (!car.getHandicap() && parking.getHandicap()){
            throw new ApiException("Sorry Parking is only for handicap");
        }


        Integer totalHours =bookingDTO.getDepartureTime().getHour() - bookingDTO.getArrivalTime().getHour();
        Double totalPrice = parking.getPrice() * totalHours;


        Company company = companyRepository.findCompanyByBranchSetContains(branch);

        if (parking.getHandicap()){
            totalPrice = 0.0;
        }

        if (customer.getBalance() < totalPrice){
            throw new ApiException("Sorry cannot update for more hours a parking balance is not enough");
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
        booking.setCar(car);
        booking.setTotalPrice(totalPrice);

        Time time = booking.getTime();
        time.setArrivalTime(bookingDTO.getArrivalTime());
        time.setDepartureTime(bookingDTO.getDepartureTime());
        time.setParking(parking);

        timeRepository.save(time);
        bookingRepository.save(booking);
    }

    public void cancelBookingParking(MyUser user,Integer bookingId){
        Booking booking = bookingRepository.findBookingById(bookingId);
        if (booking == null){
            throw new ApiException("Booking not found");
        }
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
        if (booking == null){
            throw new ApiException("Booking not found");
        }
        Customer customer = customerRepository.findCustomerByUser(user);
        if(!(customer.getId().equals(booking.getCar().getCustomer().getId()))){
            throw new ApiException("Not Authorized");
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        if (!(booking.getStatus().equalsIgnoreCase("active"))){
            throw new ApiException("Sorry you cannot check out if you did not check in");
        }
        if (localDateTime.isAfter(booking.getTime().getDepartureTime())){
            double min = localDateTime.getMinute() - booking.getTime().getDepartureTime().getMinute();
            double time = Math.round(min/60);
            double price = booking.getParking().getPrice() * time;
            booking.setTotalPrice(booking.getTotalPrice() - price);
            customer.setBalance(customer.getBalance() + price);
            Company company = booking.getParking().getBranch().getCompany();
            company.setRevenue(company.getRevenue() - price);
        }


        booking.setStatus("expired");
        bookingRepository.save(booking);
    }

    public void checkIn(MyUser user,Integer bookingId){
        Booking booking = bookingRepository.findBookingById(bookingId);
        if (booking == null){
            throw new ApiException("Booking not found");
        }
        Customer customer = customerRepository.findCustomerByUser(user);
        if(!(customer.getId().equals(booking.getCar().getCustomer().getId()))){
            throw new ApiException("Not Authorized");
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        if (booking.getTime().getArrivalTime().getMinute() - localDateTime.getMinute() > 5){
            throw new ApiException("Sorry You cannot Activate Before 5 minutes of you arriving time");
        }

        booking.setStatus("ACTIVE");
        bookingRepository.save(booking);
    }

    @Scheduled(fixedRate = 30000)
    public void changeParkingStatus(){
        LocalDateTime localDateTime = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findAll();
        for (int i = 0; i < bookings.size(); i++) {
            if (localDateTime.getMinute() - bookings.get(i).getTime().getDepartureTime().getMinute() > 30){
                if (!(bookings.get(i).getStatus().equalsIgnoreCase("expired"))){
                    bookings.get(i).setStatus("expired");
                    Double fine = bookings.get(i).getCar().getCustomer().getFine();
                    bookings.get(i).getCar().getCustomer().setFine(fine + 50);
                    bookingRepository.save(bookings.get(i));
                }

            }
            if (localDateTime.getMinute() - bookings.get(i).getTime().getArrivalTime().getMinute() > 15){
                if (!(bookings.get(i).getStatus().equalsIgnoreCase("active"))){
                   Double price = bookings.get(i).getTotalPrice() / 2;
                   Customer customer = bookings.get(i).getCar().getCustomer();
                   customer.setBalance(customer.getBalance() + price);

                   Time time = bookings.get(i).getTime();
                    time.setParking(null);

                    bookings.get(i).setTime(null);
                    timeRepository.delete(time);
                    bookingRepository.delete(bookings.get(i));
                }
            }

        }
    }

    public void checkAvailableParking(Parking parking, BookingDTO bookingDTO){
        List<Time> times = timeRepository.findAllByParking(parking);
        for (int i = 0; i < times.size(); i++) {
            if (times.get(i).getArrivalTime().equals(bookingDTO.getArrivalTime())) {
                throw new ApiException("Parking Is Booked");
            }
            if (times.get(i).getDepartureTime().equals(bookingDTO.getDepartureTime())) {
                throw new ApiException("Parking Is Booked");
            }
            if (times.get(i).getArrivalTime().isBefore(bookingDTO.getArrivalTime()) || times.get(i).getDepartureTime().isAfter(bookingDTO.getArrivalTime())) {
                throw new ApiException("Parking Is Booked");
            }
            if (times.get(i).getArrivalTime().isBefore(bookingDTO.getDepartureTime()) || times.get(i).getDepartureTime().isAfter(bookingDTO.getDepartureTime())) {
                throw new ApiException("Parking Is Booked");
            }
        }
    }






}
