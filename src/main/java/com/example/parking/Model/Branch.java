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
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "name cannot be empty ")
    @Column(columnDefinition = "varchar(25) not null")
    private String name;

    @NotEmpty(message = "location cannot be empty ")
    @Column(columnDefinition = "varchar(25) not null")
    private String location;

    @NotEmpty(message = "phone Number cannot be empty ")
    @Column(columnDefinition = "varchar(25) not null")
    private String phoneNum;


    @OneToMany(mappedBy = "branch", cascade = CascadeType.DETACH)
    @PrimaryKeyJoinColumn
    private Set<Parking> parkingSet;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "company_id",referencedColumnName = "user_id")
    private Company company;


}
