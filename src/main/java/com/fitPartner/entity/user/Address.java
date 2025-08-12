package com.fitPartner.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String houseNumber;
    private String street;
    private String city;
    private String state;
    private String country;
    private String pincode;


    @OneToOne
    @JoinColumn(name = "user_id") // Foreign key
    private MyUser user;

    public Address(String houseNumber, String street, String city, String state, String country, String pincode) {
        this.houseNumber = houseNumber;
        this.street = street;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.country = country;

    }

    public Address(String houseNumber, String street, String city, String state, String country, Object o, String pincode) {
    }
}
