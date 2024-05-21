package com.silu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.silu.dto.RestaurantDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fullName;

    private String email;

    private String password;

    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "customer"
    )
    @JsonIgnore
    @ToString.Exclude
    private List<Orders> orders = new ArrayList<>();

    @ElementCollection
    private List<RestaurantDto> favorites = new ArrayList<>();

    @OneToMany(
            cascade = CascadeType.ALL ,
            orphanRemoval = true
    )
    @ToString.Exclude
    private List<Address> addresses = new ArrayList<>();

}
