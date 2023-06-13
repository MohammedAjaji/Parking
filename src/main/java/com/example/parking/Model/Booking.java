package com.example.parking.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "car_id",referencedColumnName = "id")
    private Car car;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "parking_id",referencedColumnName = "id")
    private Parking parking;

    @OneToOne
//    @MapsId(value = "id")
    @JsonIgnore
    private Time time;



}
