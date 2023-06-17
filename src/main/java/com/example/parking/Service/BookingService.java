package com.example.parking.Service;



import com.example.parking.ApiException.ApiException;
import com.example.parking.DTO.BookingDTO;
import com.example.parking.Model.*;
import com.example.parking.Repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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

    Logger logger = LoggerFactory.getLogger(BookingService.class);
/*
        get Bookings BookingDTO
 */

    public List<Booking> getBookings(){
        return bookingRepository.findAll();
    }
    public List bookingParking(MyUser user, BookingDTO bookingDTO){
        Branch branch = branchRepository.findBranchById(bookingDTO.getBranchId());
        if (branch == null){
            throw new ApiException("Branch Not Found");
        }

        Parking parking = parkingRepository.findParkingByParkingNumber(bookingDTO.getParkingNumber());
        if (parking == null){
            throw new ApiException("Parking Not Found");
        }
        if (bookingDTO.getArrivalTime().isAfter(bookingDTO.getDepartureTime())){
            throw new ApiException("Arrival cannot be after Departure ");
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

        //bookingDTO.getDepartureTime().getHour() - bookingDTO.getArrivalTime().getHour();

        Duration duration = Duration.between(bookingDTO.getArrivalTime(), bookingDTO.getDepartureTime());
        Integer totalHours = Math.toIntExact(duration.toHours());
        double totalPrice = parking.getPrice() * totalHours;

        if (parking.getHandicap() || car.getHandicap()){
            totalPrice = 0.0;
        }


        Customer customer = customerRepository.findCustomerByUser(user);
        if (customer.getBalance() < totalPrice){
            throw new ApiException("Sorry cannot Book a parking balance is not enough");
        }

        if (bookingDTO.getUsePoints()){
            if (customer.getPoints()/10 > totalPrice){
                double point = Math.round(customer.getPoints()/10) - totalPrice;
                totalPrice = 0;
                customer.setPoints(point*10);
            }else {
                totalPrice = totalPrice - Math.round(customer.getPoints() / 10);
                customer.setPoints(0.0);
            }
        }
        double points = Math.round(totalPrice * 10);

        customer.setPoints(customer.getPoints() + points);
        customer.setBalance(customer.getBalance() - totalPrice);

        Company company = companyRepository.findCompanyByBranchSetContains(branch);
        company.setRevenue(company.getRevenue() + totalPrice);

        Booking booking = new Booking();
        booking.setParking(parking);
        booking.setCar(car);
        booking.setTotalPrice(totalPrice);
        booking.setPoints(points);
        booking.setStatus("new");

        Time time = new Time();
        time.setArrivalTime(bookingDTO.getArrivalTime());
        time.setDepartureTime(bookingDTO.getDepartureTime());
        time.setParking(parking);

        booking.setTime(time);
        time.setBooking(booking);

        timeRepository.save(time);
        bookingRepository.save(booking);
        List list = new ArrayList<>();
        list.add(totalHours);
        list.add(totalPrice);
        list.add(points);
        return list;
    }

    public List updateBookingParking(MyUser user, BookingDTO bookingDTO, Integer bookingId){
        Booking booking = bookingRepository.findBookingById(bookingId);
        if (booking == null){
            throw new ApiException("Booking not found");
        }
        Customer customer = customerRepository.findCustomerByUser(user);
        if(!(customer.getUser().getId().equals(booking.getCar().getCustomer().getUser().getId()))){
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
        if (bookingDTO.getArrivalTime().isAfter(bookingDTO.getDepartureTime())){
            throw new ApiException("Arrival cannot be after Departure ");
        }
        bookingDTO.setBookingId(bookingId);
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


        Duration duration = Duration.between(bookingDTO.getArrivalTime(), bookingDTO.getDepartureTime());
        Integer totalHours = Math.toIntExact(duration.toHours());
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
        List list = new ArrayList<>();
        list.add(totalHours);
        list.add(totalPrice);
//        list.add(points);
        return list;
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

    public double checkOut(MyUser user,Integer bookingId){
        Booking booking = bookingRepository.findBookingById(bookingId);
        if (booking == null){
            throw new ApiException("Booking not found");
        }
        Customer customer = customerRepository.findCustomerByUser(user);
        if(!(customer.getUser().getId().equals(booking.getCar().getCustomer().getUser().getId()))){
            throw new ApiException("Not Authorized");
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        if (!(booking.getStatus().equalsIgnoreCase("active"))){
            throw new ApiException("Sorry you cannot check out if you did not check in");
        }
        if (localDateTime.isAfter(booking.getTime().getDepartureTime())){
//            double min = localDateTime.getMinute() - booking.getTime().getDepartureTime().getMinute();
//            double time = Math.round(min/60);

            Duration duration = Duration.between(localDateTime, booking.getTime().getArrivalTime());
            Integer totalHours = Math.toIntExact(duration.toHours());
            double price = booking.getParking().getPrice() * totalHours;

            booking.setTotalPrice(booking.getTotalPrice() - price);
            customer.setBalance(customer.getBalance() + price);

            Company company = booking.getParking().getBranch().getCompany();
            company.setRevenue(company.getRevenue() - price);
            companyRepository.save(company);
        }

        double points = booking.getTotalPrice() * 10;
        booking.setPoints(points);
        booking.setStatus("expired");
        customer.setPoints(customer.getPoints() + points);

        customerRepository.save(customer);
        bookingRepository.save(booking);
        return points;
    }

    public void checkIn(MyUser user,Integer bookingId){
        Booking booking = bookingRepository.findBookingById(bookingId);
        if (booking == null){
            throw new ApiException("Booking not found");
        }
        Customer customer = customerRepository.findCustomerByUser(user);
        if(!(customer.getUser().getId().equals(booking.getCar().getCustomer().getUser().getId()))){
            throw new ApiException("Not Authorized");
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        Duration duration = Duration.between(booking.getTime().getArrivalTime(), localDateTime);
        Integer totalMinute = Math.toIntExact(duration.toMinutes());
        if (totalMinute < 5){
            throw new ApiException("Sorry You cannot Activate Before 5 minutes of you arriving time");
        }

        booking.setStatus("ACTIVE");
        bookingRepository.save(booking);
    }

    @Scheduled(fixedRate = 1000)
    public void changeParkingStatus(){
        LocalDateTime localDateTime = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findAll();
        for (int i = 0; i < bookings.size(); i++) {
            Duration duration = Duration.between(bookings.get(i).getTime().getDepartureTime(), localDateTime);
            Integer totalMinute = Math.toIntExact(duration.toMinutes());
            System.out.println("e: " +totalMinute);
            logger.info("Total Minutes for Booking: " + bookings.get(i).getId() + " is: " + totalMinute );
            if (totalMinute > 30){
                if (!(bookings.get(i).getStatus().equalsIgnoreCase("expired"))){
                    bookings.get(i).setStatus("expired");
                    Customer customer = bookings.get(i).getCar().getCustomer();
                    Double fine = customer.getFine();
                    customer.setFine(fine + 50);
//                    bookings.get(i).getCar().getCustomer().setFine(fine + 50);
                    customerRepository.save(customer);
                    bookingRepository.save(bookings.get(i));
                }

            }
            Duration duration1 = Duration.between(bookings.get(i).getTime().getArrivalTime(), localDateTime);
            Integer totalMinute1 = Math.toIntExact(duration1.toMinutes());
            System.out.println("a: " +totalMinute1);
            if (totalMinute1 > 15){
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
            if (bookingDTO.getBookingId()!= null &&
                    bookingDTO.getBookingId().equals(times.get(i).getBooking().getId())){
                continue;
            }
            if (times.get(i).getBooking().getStatus().equalsIgnoreCase("expired")){
                continue;
            }
            if (times.get(i).getArrivalTime().equals(bookingDTO.getArrivalTime())) {
                throw new ApiException("Parking Is Booked");
            }
            if (times.get(i).getDepartureTime().equals(bookingDTO.getDepartureTime())) {
                throw new ApiException("Parking Is Booked");
            }
            if (times.get(i).getArrivalTime().isBefore(bookingDTO.getDepartureTime()) &&
                    times.get(i).getDepartureTime().isAfter(bookingDTO.getArrivalTime())) {
                throw new ApiException("Parking Is Booked");
            }

        }
    }






}
