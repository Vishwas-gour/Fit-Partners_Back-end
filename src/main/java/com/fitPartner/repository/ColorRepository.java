package com.fitPartner.repository;

import com.fitPartner.entity.shoe.Color;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<Color,Long> {
    Color findByColorName(String colorName);
}
