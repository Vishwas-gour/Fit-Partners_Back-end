package com.fitPartner.repository;

import com.fitPartner.entity.shoe.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {
}
