package com.fitPartner.service;

import com.fitPartner.dto.MyUserResponse;
import com.fitPartner.entity.user.Address;
import com.fitPartner.entity.user.MyUser;
import com.fitPartner.repository.MyUserRepository;
import com.fitPartner.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MyUserService {
    private final MyUserRepository userrepo;
    private final AuthenticationManager manager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;


    public ResponseEntity<?> register(Map<String, String> user) {

        MyUser myUser = new MyUser ();
        myUser.setUsername (user.get ("username"));
        myUser.setEmail (user.get ("email"));
        myUser.setPassword (encoder.encode (user.get ("password")));
        myUser.setPhone (Long.parseLong (user.get ("phone")));
        myUser.setRole (user.getOrDefault ("role", "ROLE_USER"));

        Address address = new Address (
                user.get ("houseNumber"),
                user.get ("street"),
                user.get ("city"),
                user.get ("state"),
                user.get ("country"),
                user.get ("pincode")
        );

        // ✅ Set both sides of the relationship
        address.setUser (myUser);
        myUser.setAddress (address);

        // ✅ Save user (Address will be saved because of CascadeType.ALL)
        userrepo.save (myUser);

        return ResponseEntity.ok ("User Registered Successfully");
    }


    public ResponseEntity<?> login(MyUser user) {
        MyUser myUser = userrepo.findByEmail (user.getEmail ()).orElseThrow (() -> new UsernameNotFoundException ("user not found "));
        try {
            manager.authenticate (
                    new UsernamePasswordAuthenticationToken (myUser.getEmail (), user.getPassword ())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest ().body ("Wrong password");
        }
        String token = jwtUtil.generateToken (myUser.getEmail ());
        return ResponseEntity.ok (Map.of (
                "token", token,
                "email", myUser.getEmail (),
                "role", myUser.getRole ()));
    }

    public ResponseEntity<?> forgetPassword(String email, String newPassword) {
        System.out.println (email + " " + newPassword);
        MyUser myUser = userrepo.findByEmail (email).orElseThrow (() -> new UsernameNotFoundException ("user not found "));
        System.out.println (email + " " + newPassword);
        myUser.setPassword (encoder.encode (newPassword));
        userrepo.save (myUser);
        return ResponseEntity.ok ("Password changed Successfully");
    }

    public ResponseEntity<?> getProfile(String email) {
        System.out.println ("userservize: " + email);
        MyUser myUser = userrepo.findByEmail (email).orElseThrow (() -> new UsernameNotFoundException ("user not found "));
        Address address = myUser.getAddress ();
        System.out.println (address);


        return ResponseEntity.ok (Map.of (
                "id", myUser.getId (),
                "username", myUser.getUsername (),
                "email", myUser.getEmail (),
                "phone", myUser.getPhone (),
                "isBlocked", myUser.isBlocked (),

                "houseNumber", address.getHouseNumber (),
                "street", address.getStreet (),
                "state", address.getState (),
                "city", address.getCity (),
                "pincode", address.getPincode ()
        ));
    }

    public boolean getAvailableDeliveryBoy(String email) {
        String pincode = userrepo.findByEmail (email).orElseThrow (() -> new UsernameNotFoundException ("user not found ")).getAddress ().getPincode ();
        List<MyUser> list = userrepo.findFirstByRoleAndAddress_Pincode ("ROLE_EMPLOYEE", pincode);
        return !list.isEmpty ();
    }


    public ResponseEntity<?> findByRole(String role) {
        System.out.println (role + " " + "userservize");
        List<MyUser> list = userrepo.findByRole (role);
        List<MyUserResponse> responseList = list.stream ().map (user -> new MyUserResponse (user)).toList ();
        System.out.println (responseList);
        return ResponseEntity.ok (responseList);
    }

    public List<MyUser> findByRoleAndAddress_Pincode(String employee, String pincode) {
        return userrepo.findFirstByRoleAndAddress_Pincode (employee, pincode);
    }

    public ResponseEntity<?> toggleBlock(String email) {
        MyUser myUser = userrepo.findByEmail (email).orElseThrow (() -> new UsernameNotFoundException ("user not found "));
        myUser.setIsBlocked (!myUser.isBlocked ());
        userrepo.save (myUser);
        return ResponseEntity.ok ("User Blocked Successfully");
    }


    public Long findByEmail(String name) {
        return userrepo.findByEmail (name).orElseThrow (() -> new UsernameNotFoundException ("user not found ")).getId ();
    }
}
