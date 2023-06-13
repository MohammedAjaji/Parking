package com.example.parking.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotEmpty(message = "parking Number cannot be empty ")
    @Column(columnDefinition = "varchar(10) not null")
    private String parkingNumber;

    private Boolean outdoor = true;

    private Boolean handicap = false;

    @Column(columnDefinition = "int not null")
    private Integer floor;

    @NotNull(message = "Price cannot be null")
    @Column(columnDefinition = "decimal not null")
    private Double price;


    @OneToMany(mappedBy = "parking", cascade = CascadeType.DETACH)
    @PrimaryKeyJoinColumn
    private Set<Time> timeSet;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "branch_id",referencedColumnName = "id")
    private Branch branch;

    @OneToMany(mappedBy = "parking", cascade = CascadeType.DETACH)
    @PrimaryKeyJoinColumn
    private Set<Booking> bookingSet;




}
