package com.fitPartner.repository;

import com.fitPartner.entity.user.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MyUserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByUsername(String username);

    Optional<MyUser> findByEmail(String email);

    MyUser findFirstByRoleAndAddress_City(String employee, String city);

    List<MyUser> findByRoleAndAddress_City(String roleDeliveryBoy, String city);

    List<MyUser> findByRole(String role);

    List<MyUser> findFirstByRoleAndAddress_Pincode(String employee, String pincode);
}

