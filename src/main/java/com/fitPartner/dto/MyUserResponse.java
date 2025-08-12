package com.fitPartner.dto;

import com.fitPartner.entity.user.MyUser;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MyUserResponse {
    private Long id;
    private String username;
    private String email;
    private String role;
    private long phone;
    private boolean isBlocked;
    private LocalDateTime createdAt;
    private String houseNumber;
    private String street;
    private String city;
    private String state;
    private String country;
    private String pincode;

    public MyUserResponse(MyUser user) {
        this.id = user.getId ();
        this.username = user.getUsername ();
        this.email = user.getEmail ();
        this.role = user.getRole ();
        this.phone = user.getPhone ();
        this.isBlocked = user.isBlocked ();
        this.createdAt = user.getCreatedAt ();
        this.houseNumber = user.getAddress ().getHouseNumber ();
        this.street = user.getAddress ().getStreet ();
        this.city = user.getAddress ().getCity ();
        this.state = user.getAddress ().getState ();
        this.pincode = user.getAddress ().getPincode ();
    }

    @Override
    public String toString() {
        return "MyUserResponse{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", phone=" + phone +
                ", isBlocked=" + isBlocked +
                ", createdAt=" + createdAt +
                ", houseNumber='" + houseNumber + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", pincode='" + pincode + '\'' +
                '}';
    }
}
