package com.example.parking.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Parking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String parkingNumber;

    private Boolean outdoor;
    private Boolean handicap;

    private Double price;


    private Integer floor;

    @OneToMany(mappedBy = "parking", cascade = CascadeType.DETACH)
    @PrimaryKeyJoinColumn
    private Set<Time> timeSet;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "place_id",referencedColumnName = "id")
    private Place place;

    @OneToMany(mappedBy = "parking", cascade = CascadeType.DETACH)
    @PrimaryKeyJoinColumn
    private Set<Booking> bookingSet;




}
