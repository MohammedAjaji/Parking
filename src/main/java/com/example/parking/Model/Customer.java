package com.example.parking.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {

    @Id
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
    @Column(columnDefinition = "decimal not null")
    private Double balance;


    private Double fine;

    private Double points;

    @OneToOne
    @MapsId
    @JsonIgnore
    private MyUser user;



    @OneToMany(mappedBy = "customer", cascade = CascadeType.DETACH)
    @PrimaryKeyJoinColumn
    private Set<Car> carSet;







}
