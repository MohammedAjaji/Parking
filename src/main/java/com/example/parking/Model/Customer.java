package com.example.parking.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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


    @NotEmpty(message = "first name must be not null")
    private String firstName;
    private String lastName;

    private String phoneNum;

    private Double balance;

    @OneToOne
//    @MapsId(value = "id")
    @JsonIgnore
    private MyUser user;



    @OneToMany(mappedBy = "customer", cascade = CascadeType.DETACH)
    @PrimaryKeyJoinColumn
    private Set<Car> carSet;







}
