package com.example.parking.DTO;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {

    private Integer bookingId;

//    @NotNull(message ="carId should not be empty" )
    private Integer carId;

//    @NotNull(message ="branchId should not be empty" )
    private Integer branchId;

//    @NotNull(message ="parkingNumber should not be empty" )
    private String parkingNumber;


    private Boolean usePoints;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @FutureOrPresent
    private LocalDateTime arrivalTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @FutureOrPresent
    private LocalDateTime departureTime;
}
