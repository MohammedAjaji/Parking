package com.example.parking.Controller;


import com.example.parking.DTO.BookingDTO;
import com.example.parking.Model.Booking;
import com.example.parking.Model.MyUser;
import com.example.parking.Model.Parking;
import com.example.parking.Service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;


    @GetMapping("/get")
    public ResponseEntity getBookings(){
        List<Booking> bookings = bookingService.getBookings();
        return ResponseEntity.status(200).body(bookings);
    }
    @PostMapping("/parking")
    public ResponseEntity bookingParking(@AuthenticationPrincipal MyUser user, @Valid @RequestBody BookingDTO bookingDTO){
        List list = bookingService.bookingParking(user,bookingDTO);
        return ResponseEntity.status(200).body("Booking Done " +
                "\nTotal Hours is " + list.get(0) +
                "\nTotal Price is " + list.get(1));
    }

    @PutMapping("/update/{bookingId}")
    public ResponseEntity updateBooking(@AuthenticationPrincipal MyUser user, @Valid @RequestBody BookingDTO bookingDTO, @PathVariable Integer bookingId){
        List list = bookingService.updateBookingParking(user, bookingDTO,bookingId);
        return ResponseEntity.status(200).body("Booking Updated " +
                "\nTotal Hours is " + list.get(0) +
                "\nTotal Price is " + list.get(1));
    }

    @DeleteMapping("/cancel/{bookingId}")
    public ResponseEntity cancelBooking(@AuthenticationPrincipal MyUser user, @PathVariable Integer bookingId){
        bookingService.cancelBookingParking(user,bookingId);
        return ResponseEntity.status(200).body("Booking Cancel");
    }

    @PutMapping("/checkout/{bookingId}")
    public ResponseEntity checkOut(@AuthenticationPrincipal MyUser user, @PathVariable Integer bookingId){
        double points = bookingService.checkOut(user, bookingId);
        return ResponseEntity.status(200).body("Check Out" +
                "\n Total Points is " + points +
                " \n(Thank You for using ParKing) ");
    }

    @PutMapping("/checkin/{bookingId}")
    public ResponseEntity checkIn(@AuthenticationPrincipal MyUser user, @PathVariable Integer bookingId){
        bookingService.checkIn(user, bookingId);
        return ResponseEntity.status(200).body("Check In (Thank You for using ParKing) ");
    }

    @GetMapping("/get-parking/{bookingId}")
    public ResponseEntity getBookingParking(@AuthenticationPrincipal MyUser user, @PathVariable Integer bookingId){
       String parking =  bookingService.getBookingParking(bookingId);
        return ResponseEntity.status(200).body("Parking number is: " + parking);
    }
    @GetMapping("/get-location/{bookingId}")
    public ResponseEntity getBookingLocation(@AuthenticationPrincipal MyUser user, @PathVariable Integer bookingId){
        String location = bookingService.getBookingLocation(bookingId);
        return ResponseEntity.status(200).body("location is at: " + location );
    }
}
