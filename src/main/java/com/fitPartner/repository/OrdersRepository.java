package com.fitPartner.repository;


import com.fitPartner.entity.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByUserEmail(String name);

    Integer countByUserEmail(String name);


    List<Orders> findByAssignedTo_Id(Long id);
}
