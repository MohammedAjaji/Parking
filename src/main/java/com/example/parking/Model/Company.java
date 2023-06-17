package com.example.parking.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Company {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "name cannot be empty ")
    @Column(columnDefinition = "varchar(25) not null")
    private String name;

    @Column(columnDefinition = "varchar(25) not null check (status='pending' or status='approved' or status='disapproved')")
    private String status;

    @Column(columnDefinition = "decimal not null")
    private Double revenue;

    @OneToOne
    @MapsId
    @JsonIgnore
    private MyUser user;

    @OneToMany(mappedBy = "company", cascade = CascadeType.DETACH)
    @PrimaryKeyJoinColumn
    private Set<Branch> branchSet;
}
