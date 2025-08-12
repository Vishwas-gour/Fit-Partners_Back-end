package com.fitPartner.repository;

import com.fitPartner.entity.shoe.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SizeRepository extends JpaRepository<Size, Long> {
    Size findBySizeNo(int sizeNo);
}
