package com.silu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToOne
    private User owner;

    private String description;

    private String cuisineType;

    @OneToOne
    private Address address;

    @Embedded
    private ContactInformation contactInformation;

    private String openingHours;

    @OneToMany(
            mappedBy = "restaurant",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Orders> orders = new ArrayList<Orders>();

    @ElementCollection
    @Column(length = 1000)
    private List<String> images ;

    private LocalDateTime registrationDate;

    private boolean open;

    @OneToMany(
            mappedBy = "restaurant",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<Food> foods = new ArrayList<Food>();
}
