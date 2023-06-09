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
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNum;
    private Boolean handicap;

    private Double balance;

    @OneToOne
//    @MapsId(value = "id")
    @JsonIgnore
    private MyUser user;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.DETACH)
    @PrimaryKeyJoinColumn
    private Set<Time> timeSet;

    @ManyToMany(mappedBy = "customerSet")
    private Set<Parking> parkingSet;

}
