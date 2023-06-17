package com.example.parking.Controller;

import com.example.parking.Controller.BookingController;
import com.example.parking.DTO.BookingDTO;
import com.example.parking.Model.MyUser;
import com.example.parking.Service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;


class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void bookingParking_shouldReturnBookingDoneMessage() {
        // Arrange
        MyUser user = new MyUser();
        BookingDTO bookingDTO = new BookingDTO(1,1,"P1", LocalDateTime.now(),LocalDateTime.now().plusHours(2));

        // Act
        ResponseEntity<String> response = bookingController.bookingParking(user, bookingDTO);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Booking Done", response.getBody());
        Mockito.verify(bookingService, Mockito.times(1)).bookingParking(user, bookingDTO);
    }

    @Test
    void updateBooking_shouldReturnBookingUpdatedMessage() {
        // Arrange
        MyUser user = new MyUser();
        BookingDTO bookingDTO = new BookingDTO(1,1,"P1", LocalDateTime.now(),LocalDateTime.now().plusHours(6));
        Integer bookingId = 1;

        // Act
        ResponseEntity<String> response = bookingController.updateBooking(user, bookingDTO, bookingId);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Booking updated", response.getBody());
        Mockito.verify(bookingService, Mockito.times(1)).updateBookingParking(user, bookingDTO, bookingId);
    }

    @Test
    void cancelBooking_shouldReturnBookingCancelledMessage() {
        // Arrange
        MyUser user = new MyUser();
        Integer bookingId = 1;

        // Act
        ResponseEntity<String> response = bookingController.cancelBooking(user, bookingId);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Booking Cancel", response.getBody());
        Mockito.verify(bookingService, Mockito.times(1)).cancelBookingParking(user, bookingId);
    }
}
