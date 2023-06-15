package com.example.parking.Repository;

import com.example.parking.Model.Parking;
import com.example.parking.Model.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeRepository extends JpaRepository<Time, Integer> {

    List<Time> findAllByParking(Parking parking);

    Time findTimeByArrivalTimeAndDepartureTime(LocalDateTime a, LocalDateTime d);


    @Query("SELECT t FROM Time t WHERE t.arrivalTime <= :startDateTime OR t.departureTime >= :endDateTime")
    List<Time> findAvailableTimes(LocalDateTime startDateTime, LocalDateTime endDateTime);

}
