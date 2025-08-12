package com.fitPartner.repository;

import com.fitPartner.entity.shoe.Shoe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoesRepository extends JpaRepository<Shoe, Long> {

    List<Shoe> findByGender(String gender);

     List<Shoe> findTop10ByBrandOrCategoryOrGenderOrMaterial(String brand, String category, String gender, String material);
    List<Shoe> findByNameContainingIgnoreCase(String name);
    List<Shoe> findTop7ByGender(String gender);

}
