package com.example.parking.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "first name should not be empty")
    @Column(columnDefinition = "varchar(20) not null")
    private String firstName;

    @NotEmpty(message = "last name should not be empty")
    @Column(columnDefinition = "varchar(20) not null")
    private String lastName;

    @NotEmpty(message = "phone number should not be empty")
    @Column(columnDefinition = "varchar(20) not null")
    private String phoneNum;

    @NotNull(message ="balance should not be empty" )
    @Positive(message = "please enter positive number")
    @Column(columnDefinition = "double not null")
    private Double balance;

    @OneToOne
    @MapsId
    @JsonIgnore
    private MyUser user;



    @OneToMany(mappedBy = "customer", cascade = CascadeType.DETACH)
    @PrimaryKeyJoinColumn
    private Set<Car> carSet;







}
