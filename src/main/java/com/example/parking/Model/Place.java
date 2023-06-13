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
    private String location;
    private String email;
    private String phoneNum;


    @OneToMany(mappedBy = "place", cascade = CascadeType.DETACH)
    @PrimaryKeyJoinColumn
    private Set<Parking> parkingSet;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "company_id",referencedColumnName = "id")
    private Company company;


}
