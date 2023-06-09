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
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String email;
    private String phoneNum;

    private Double revenue;

    @OneToOne
//    @MapsId(value = "id")
    @JsonIgnore
    private MyUser user;

    @OneToMany(mappedBy = "place", cascade = CascadeType.DETACH)
    @PrimaryKeyJoinColumn
    private Set<Parking> parkingSet;
}
