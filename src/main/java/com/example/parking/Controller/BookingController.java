package com.example.parking.Controller;


import com.example.parking.DTO.BookingDTO;
import com.example.parking.Model.MyUser;
import com.example.parking.Service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/parking")
    public ResponseEntity bookingParking(@AuthenticationPrincipal MyUser user, @Valid @RequestBody BookingDTO bookingDTO){
        bookingService.bookingParking(user,bookingDTO);
        return ResponseEntity.status(200).body("Booking Done");
    }

    @PutMapping("/update/{bookingId}")
    public ResponseEntity updateParking(@AuthenticationPrincipal MyUser user, @Valid @RequestBody BookingDTO bookingDTO, @PathVariable Integer bookingId){
        bookingService.updateBookingParking(user, bookingDTO,bookingId);
        return ResponseEntity.status(200).body("Booking updated");
    }

    @DeleteMapping("/update/{bookingId}")
    public ResponseEntity updateParking(@AuthenticationPrincipal MyUser user, @PathVariable Integer bookingId){
        bookingService.cancelBookingParking(user,bookingId);
        return ResponseEntity.status(200).body("Booking Cancel");
    }
}
