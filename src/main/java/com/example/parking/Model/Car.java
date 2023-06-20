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
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "name should not be empty")
    @Column(columnDefinition = "varchar(20) not null")
    private String name;

    @NotEmpty(message = "licensePlate should not be empty")
    @Column(columnDefinition = "varchar(20) not null")
    private String licensePlate;

    @NotEmpty(message = "color should not be empty")
    @Column(columnDefinition = "varchar(20) not null")
    private String color;

    private Boolean handicap = false;



    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "customer_id",referencedColumnName = "user_id")
    private Customer customer;

    @OneToMany(mappedBy = "car", cascade = CascadeType.DETACH)
    @PrimaryKeyJoinColumn
    private Set<Booking> bookingSet;


}
