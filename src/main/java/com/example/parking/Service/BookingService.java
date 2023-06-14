package com.example.parking.Service;


import com.example.parking.Repository.BookingRepository;
import com.example.parking.Repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

}
