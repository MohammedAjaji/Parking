package com.example.parking.Service;


import com.example.parking.Repository.TimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TimeService {

    private final TimeRepository timeRepository;

}
