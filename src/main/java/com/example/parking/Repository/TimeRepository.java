package com.example.parking.Repository;

import com.example.parking.Model.Parking;
import com.example.parking.Model.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeRepository extends JpaRepository<Time, Integer> {

    List<Time> findAllByParking(Parking parking);
}
