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
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String licensePlate;

    private String color;

    private Boolean handicap;



    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "customer_id",referencedColumnName = "id")
    private Customer customer;

    @OneToMany(mappedBy = "car", cascade = CascadeType.DETACH)
    @PrimaryKeyJoinColumn
    private Set<Booking> bookingSet;


}
