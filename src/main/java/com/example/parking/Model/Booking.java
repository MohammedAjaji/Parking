package com.example.parking.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Price cannot be null")
    @Column(columnDefinition = "decimal not null")
    private Double totalPrice;

    @Column(columnDefinition = "varchar(25) not null check (status='new' or status='active' or status='expired')")
    private String status;

    private Double points;


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "car_id",referencedColumnName = "id")
    private Car car;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "parking_id",referencedColumnName = "id")
    private Parking parking;


    @OneToOne(cascade = CascadeType.ALL, mappedBy = "booking")
    @PrimaryKeyJoinColumn
    private Time time;



}
